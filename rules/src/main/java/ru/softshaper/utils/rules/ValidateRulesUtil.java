package ru.softshaper.utils.rules;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import ru.softshaper.services.drools.IRuleDisigner;
import ru.softshaper.services.utils.IResultUtil;
import ru.softshaper.services.utils.IUtil;
import ru.softshaper.services.utils.IUtilsEngine;
import ru.softshaper.services.utils.StepUtil;
import ru.softshaper.services.utils.bean.impl.ResultUtil;

@Component
public class ValidateRulesUtil implements IUtil {

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
    return "Проверка";
  }

  /*
   * (non-Javadoc)
   * 
   * @see ru.softshaper.services.utils.IUtil#getCode()
   */
  @Override
  public String getCode() {
    return "ValidateRulesUtil";
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
    Collection<String> errorMessages = Lists.newArrayList();
    for (Message e : kieBuilder.getResults().getMessages()) {
      errorMessages.add(e.getText());
    }
    return new ResultUtil(errorMessages.isEmpty() ? "Правила корректны" : null, !errorMessages.isEmpty(), errorMessages);
  }
}
