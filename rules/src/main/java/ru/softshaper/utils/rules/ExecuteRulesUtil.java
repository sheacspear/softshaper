package ru.softshaper.utils.rules;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.drools.compiler.kie.builder.impl.KieBuilderImpl;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.drools.core.SessionConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import ru.softshaper.services.drools.IRuleDisigner;
import ru.softshaper.services.drools.contex.RuleContext;
import ru.softshaper.services.drools.parser.Parser;
import ru.softshaper.services.drools.provider.DataProvider;
import ru.softshaper.services.drools.saver.Saver;
import ru.softshaper.services.utils.IResultUtil;
import ru.softshaper.services.utils.IUtil;
import ru.softshaper.services.utils.IUtilsEngine;
import ru.softshaper.services.utils.StepUtil;
import ru.softshaper.services.utils.bean.impl.ResultUtil;
import ru.softshaper.storage.jooq.tables.daos.DcRuleDao;
import ru.softshaper.storage.jooq.tables.daos.DcSuitRulesDao;
import ru.softshaper.storage.jooq.tables.pojos.DcRule;
import ru.softshaper.storage.jooq.tables.pojos.DcSuitRules;

@Component
public class ExecuteRulesUtil implements IUtil {
  public static final Logger log = LoggerFactory.getLogger(ExecuteRulesUtil.class);
  @Autowired
  private IRuleDisigner ruleDisigner;

  @Autowired
  private DcRuleDao dcRuleDao;

  @Autowired
  private DcSuitRulesDao dcSuitRulesDao;

  @Autowired
  private IUtilsEngine utilsEngine;
  
  
  @Autowired
  private Parser parser;
  @Autowired
  private DataProvider dataProvider;
  @Autowired
  private Saver saver;
  
  
  

  @PostConstruct
  public void init() {
    utilsEngine.registerObjectUtil(this, "suitRules");
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.utils.IUtil#getName()
   */
  @Override
  public String getName() {
    return "Выполнение";
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.utils.IUtil#getCode()
   */
  @Override
  public String getCode() {
    return "ExecuteRulesUtil";
  }

  @Override
  public StepUtil getNextStep(Map<String, Object> data) {
    return null;
  }

  @Override
  public IResultUtil execute(String metaClazz, String objId, Map<String, Object> data) {
    try {
      KieServices ks = KieServices.Factory.get();
      KieFileSystem kfs = ks.newKieFileSystem();
      // плучить сюит, сгенерить Pom.xml
      DcSuitRules suit = dcSuitRulesDao.fetchOneById(Long.valueOf(objId));
      ReleaseId releaseId = new ReleaseIdImpl(suit.getGroupid(), suit.getArtifactid(), suit.getVersion());
      // получить правила
      Collection<DcRule> dcRules = dcRuleDao.fetchByRulessuit(suit.getId());
      for (DcRule e : dcRules) {
        kfs.write("src/main/resources/" + e.getName() + ".drl", ResourceFactory.newByteArrayResource(e.getDescr().getBytes()));
      }
      kfs.generateAndWritePomXML(releaseId);

      KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
      Collection<String> errorMessages = Lists.newArrayList();
      for (Message e : kieBuilder.getResults().getMessages()) {
        errorMessages.add(e.getText());
      }
      if (!errorMessages.isEmpty()) {
        return new ResultUtil(null, true, errorMessages);
      }
      KieContainer kieContainer = ks.newKieContainer(releaseId);
      KieSessionConfiguration conf = SessionConfiguration.getDefaultInstance();
      KieSession kSession = kieContainer.newKieSession(conf);
      kSession.setGlobal("dataProvider", dataProvider);
      kSession.setGlobal("parser", parser);
      kSession.setGlobal("saver", saver);
      RuleContext object = new RuleContext();
      kSession.insert(object);
      int resultCnt = kSession.fireAllRules();

      Collection<String> msgsD = Lists.newArrayList();
      msgsD.add("Выполнено правил: " + resultCnt);
      Multimap<String, String> msgs = object.getMsgs();
      for (Entry<String, String> entry : msgs.entries()) {
        msgsD.add("Правило " + entry.getKey() + " сообщение:" + entry.getValue());
        // System.out.println("Правило " + entry.getKey() + " сообщение:" +
        // entry.getValue());
      }
      return new ResultUtil("Выполнено правил: " + resultCnt, true, msgsD);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultUtil(null, true, Arrays.asList(e.getMessage()));
    }
  }
}
