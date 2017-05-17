package ru.softshaper.services.drools.impl;

import java.io.File;

import org.drools.core.SessionConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.softshaper.services.drools.IRuleDisigner;
import ru.softshaper.services.drools.SuitResult;
import ru.softshaper.services.drools.contex.RuleContext;
import ru.softshaper.services.drools.parser.Parser;
import ru.softshaper.services.drools.provider.DataProvider;
import ru.softshaper.services.drools.saver.Saver;

@Component
public class RuleDisignerBase implements IRuleDisigner {

  @Autowired
  private KieServices ks;

  @Override
  public String validateRule(String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String validateSuit(String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SuitResult runSuit(String id) {
    KieFileSystem kfs = ks.newKieFileSystem();
    // kfs.write(ResourceFactory.newFileResource(new
    // File("D:\\Documents\\Dropbox\\BIO\\ruleDesigner\\src\\main\\resources\\rules\\kanban.drl")));
    kfs.write(ResourceFactory.newFileResource(new File("C:\\work\\framework\\rules\\src\\main\\resources\\rules\\kanban.drl")));
    kfs.write(ResourceFactory.newFileResource(new File("C:\\work\\framework\\rules\\src\\main\\resources\\rules\\parse.drl")));
    kfs.write(ResourceFactory.newFileResource(new File("C:\\work\\framework\\rules\\src\\main\\resources\\rules\\save.drl")));
    KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();

    kieBuilder.getResults().getMessages().forEach(e -> System.out.println(e.toString()));

    KieContainer kieContainer = ks.newKieContainer(ks.getRepository().getDefaultReleaseId());
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

    // TODO Auto-generated method stub
    return null;
  }
}
