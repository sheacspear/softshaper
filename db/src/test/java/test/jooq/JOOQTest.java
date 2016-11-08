package test.jooq;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.zorb.conf.JooqConfig;

import javax.sql.DataSource;
import java.math.BigInteger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JooqConfig.class})
//@WebAppConfiguration
@ComponentScan("ru.zorb")
public class JOOQTest {

    @Autowired
    DataSource dataSource;

    @org.junit.Ignore
    @org.junit.Test
    public void test() {
        try {
            DSLContext databse = DSL.using(dataSource.getConnection(), SQLDialect.H2);
            databse.createTable("TEST_TABLE")
                    .column("COL1", SQLDataType.BIGINT)
                    .column("COL2", SQLDataType.VARCHAR)
                    .column("COL3", SQLDataType.DOUBLE)
                    .execute();
            databse.insertInto(DSL.table("TEST_TABLE"))
                    .set(DSL.field("COL1"), BigInteger.TEN)
                    .set(DSL.field("COL2"), "HELLO ALEX")
                    .set(DSL.field("COL3"), 12.12)
                    .execute();/*
            DSL.field("COL1", BigInteger.class),
                    DSL.field("COL2", String.class),
                    DSL.field("COL3", Double.class)*/
            Result<Record> result = databse
                    .select(new SelectField[]{DSL.field("COL1", BigInteger.class),
                            DSL.field("COL2", String.class),
                            DSL.field("COL3", Double.class)})
                    .from("TEST_TABLE")
                    .limit(1)
                    .fetch();
            Record record = result.get(0);
            Assert.assertEquals(BigInteger.TEN, record.getValue(DSL.<BigInteger>field("COL1")));
            Assert.assertEquals("HELLO ALEX", record.getValue(DSL.<String>field("COL2")));
            Assert.assertEquals(12.12d, record.getValue(DSL.field("COL3", Double.class)), 0.01d);
            System.out.println(result);
        }

        // For the sake of this tutorial, let's keep exception handling simple
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
