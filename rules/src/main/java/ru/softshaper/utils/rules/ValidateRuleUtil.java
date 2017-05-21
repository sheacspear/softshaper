package ru.softshaper.utils.rules;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import ru.softshaper.services.drools.IRuleDisigner;
import ru.softshaper.services.utils.IResultUtil;
import ru.softshaper.services.utils.IUtil;
import ru.softshaper.services.utils.IUtilsEngine;
import ru.softshaper.services.utils.StepUtil;
import ru.softshaper.services.utils.bean.impl.ResultUtil;
import ru.softshaper.storage.jooq.tables.daos.DcRuleDao;
import ru.softshaper.storage.jooq.tables.pojos.DcRule;

@Component
public class ValidateRuleUtil implements IUtil {
  public static final Logger log = LoggerFactory.getLogger(ValidateRuleUtil.class);

  @Autowired
  private IRuleDisigner ruleDisigner;

  @Autowired
  private DcRuleDao dcRuleDao;

  @Autowired
  private IUtilsEngine utilsEngine;

  @PostConstruct
  public void init() {
    utilsEngine.registerObjectUtil(this, "rule");
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.utils.IUtil#getName()
   */
  @Override
  public String getName() {
    return "Проверка";
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.utils.IUtil#getCode()
   */
  @Override
  public String getCode() {
    return "ValidateRuleUtil";
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
      ReleaseId releaseId = new ReleaseIdImpl("test", "test", "0.0");
      // получить правила
      Collection<DcRule> dcRules = dcRuleDao.fetchById(Long.valueOf(objId));
      for (DcRule e : dcRules) {
        kfs.write("src/main/resources/" + e.getName() + ".drl", ResourceFactory.newByteArrayResource(e.getDescr().getBytes()));
      }
      kfs.generateAndWritePomXML(releaseId);
      KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
      Collection<String> errorMessages = Lists.newArrayList();
      for (Message e : kieBuilder.getResults().getMessages()) {
        errorMessages.add(e.getText());
      }
      return new ResultUtil(errorMessages.isEmpty() ? "Правило корректно" : null, !errorMessages.isEmpty(), errorMessages);
    } catch (Exception e) {
      log.error(e.getMessage(),e);
      return new ResultUtil(null, true, Arrays.asList(e.getMessage()));
    }
  }
}
