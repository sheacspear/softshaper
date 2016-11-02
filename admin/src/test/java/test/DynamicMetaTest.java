package test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ru.zorb.conf.MetaConfig;

/**
 * Created by arostov on 05.08.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MetaConfig.class})
@Transactional
public class DynamicMetaTest {

  public static final Logger log = LoggerFactory.getLogger(DynamicMetaTest.class);
/*
  @Autowired
  private DataSource<Record> dataSource;

  @Autowired
  private MetaStorage metaStorage;

  private MetaFieldBean createField(String fieldCode, String fieldName, String fieldColumn) {
    return new MetaFieldBean(null, null, fieldName, fieldCode, fieldColumn, "STRING", null, null);
  }

  @Rollback
  @Test
  public void test() {
    MetaClass content = create("TestBean1");

    MetaClassBean dc = update(content);

    dc.getDataSource().deleteObject("TestBean1", dc.getId());
  }

  // @Test
  public void create() {
    create("TestBean1");
  }

  // @Test
  public void update() {
    MetaClass content = metaStorage.getMetaClass("TestBean1");
    update(content);
  }

  // @Test
  public void delete() {
    MetaClass content = metaStorage.getMetaClass("TestBean1");
    content.getDataSource().deleteObject("TestBean1", content.getId());
  }

  private MetaClassBean update(MetaClass content) {
    Collection<Record> objects;
    String testBeanCode = content.getCode();
    Collection<MetaFieldBean> fields = Sets.newHashSet();
    fields.add(createField("field1", "field1", "field1"));
    fields.add(createField("field2", "field2", "field2"));
    fields.add(createField("field3", "field3", "field3"));
    MetaClassBean dc = new MetaClassBean(content.getId(), dataSource, testBeanCode, "Тестовый бин", "dc_test_bean1", fields, false, false);
    dc.getDataSource().updateObject(content.getCode(), );
    content = metaDynamicContentDao.getContent(testBeanCode);
    Assert.assertNotNull(content);
    log.debug(content.toString());

    Map<String, Object> data = Maps.newHashMap();
    data.put("field1", "data1");
    data.put("field2", "data2");
    data.put("field3", "data3");
    dataSource.createObject(testBeanCode, data);
    objects = dataSource.getObjList(testBeanCode);
    Assert.assertNotNull(objects);
    Assert.assertEquals(objects.size(), 2);
    log.debug(objects.toString());
    return dc;
  }

  private MetaClass create(String testBeanCode) {
    MetaClass content;
    Collection<Record> objects;
    Collection<MetaFieldBean> fields = Sets.newHashSet();
    fields.add(createField("field1", "field1", "field1"));
    fields.add(createField("field2", "field2", "field2"));

    MetaClassBean personCard = new MetaClassBean(null, testBeanCode, "Тестовый бин", "dc_test_bean1", fields,  false, false);

    metaDynamicContentDao.create(personCard);
    content = metaDynamicContentDao.getContent(testBeanCode);
    Assert.assertNotNull(content);
    log.debug(content.toString());

    Map<String, Object> data = Maps.newHashMap();
    data.put("field1", "data1");
    data.put("field2", "data2");
    dataSource.createObject(testBeanCode, data);
    objects = dataSource.getObjList(testBeanCode);
    Assert.assertNotNull(objects);
    Assert.assertEquals(objects.size(), 1);
    log.debug(objects.toString());
    return content;
  }
*/
}
