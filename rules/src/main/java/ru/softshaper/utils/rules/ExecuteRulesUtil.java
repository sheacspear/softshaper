package ru.softshaper.utils.rules;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.drools.core.SessionConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import ru.softshaper.services.drools.IRuleDisigner;
import ru.softshaper.services.drools.contex.RuleContext;
import ru.softshaper.services.drools.parser.Parser;
import ru.softshaper.services.drools.provider.DataProvider;
import ru.softshaper.services.drools.saver.Saver;
import ru.softshaper.services.utils.IUtil;
import ru.softshaper.services.utils.IUtilsEngine;
import ru.softshaper.services.utils.IResultUtil;
import ru.softshaper.services.utils.StepUtil;
import ru.softshaper.services.utils.bean.impl.ResultUtil;

@Component
public class ExecuteRulesUtil implements IUtil {

  @Autowired
  private IRuleDisigner ruleDisigner;

  @Autowired
  private IUtilsEngine utilsEngine;

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

    // load up the knowledge base
    KieServices ks = KieServices.Factory.get();
    KieFileSystem kfs = ks.newKieFileSystem();
    // kfs.write(ResourceFactory.newFileResource(new
    // File("D:\\Documents\\Dropbox\\BIO\\ruleDesigner\\src\\main\\resources\\rules\\kanban.drl")));
    kfs.write(ResourceFactory.newFileResource(new File("C:\\work\\framework\\rules\\src\\main\\resources\\rules\\kanban.drl")));
    kfs.write(ResourceFactory.newFileResource(new File("C:\\work\\framework\\rules\\src\\main\\resources\\rules\\parse.drl")));
    kfs.write(ResourceFactory.newFileResource(new File("C:\\work\\framework\\rules\\src\\main\\resources\\rules\\save.drl")));
    kfs.write("pom.xml", ResourceFactory.newFileResource(new File("C:\\work\\framework\\rules\\src\\main\\resources\\rules\\pom.xml")));
    KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
    kieBuilder.getResults().getMessages().forEach(e -> System.out.println(e.toString()));

    Collection<String> errorMessages = Lists.newArrayList();
    for (Message e : kieBuilder.getResults().getMessages()) {
      errorMessages.add(e.getText());
    }

    if (!errorMessages.isEmpty()) {
      return new ResultUtil(null, true, errorMessages);
    }

    // KieContainer kieContainer =
    // ks.newKieContainer(ks.getRepository().getDefaultReleaseId());
    KieContainer kieContainer = ks.newKieContainer(new ReleaseIdImpl("ru.softshaper", "drools", "0.0.1-SNAPSHOT"));
    KieSessionConfiguration conf = SessionConfiguration.getDefaultInstance();
    KieSession kSession = kieContainer.newKieSession(conf);
    // load up the knowledge base
    // KieServices ks = KieServices.Factory.get();
    // KieContainer kContainer = ks.getKieClasspathContainer();
    // KieSession kSession = kContainer.newKieSession("ksession-rules");

    // DroolsIntroduction droolsIntroduction = new
    // DroolsIntroduction("Drools");
    // kSession.insert(droolsIntroduction);
    // kSession.insert(new DroolsIntroduction("spring"));
    kSession.setGlobal("dataProvider", new DataProvider());
    kSession.setGlobal("parser", new Parser());
    kSession.setGlobal("saver", new Saver());
    RuleContext object = new RuleContext();
    kSession.insert(object);
    // kSession.getAgenda().activateRuleFlowGroup("Group1");
    kSession.fireAllRules();
    return new ResultUtil("Правила выполнены", false, null);
  }
}
