package lu.jimenez.research.mylittleplugin;

import org.junit.jupiter.api.Test;
import org.mwg.task.ActionFunction;
import org.mwg.task.TaskContext;

import static lu.jimenez.research.mylittleplugin.MyLittleActions.keepFirstResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mwg.internal.task.CoreActions.*;
import static org.mwg.task.Tasks.newTask;

class ActionKeepFirstResultTest extends ActionTest {

    @Test
    public void test() {
        initGraph();
        final int[] counter ={0};
        newTask()
                .then(readGlobalIndex("nodes"))
                .then(traverse("children"))
                .thenDo(new ActionFunction() {
                    public void eval(TaskContext context) {
                        assertEquals(context.result().size(),2);
                        counter[0]++;
                        context.continueTask();
                    }
                })
                .then(keepFirstResult())
                .thenDo(new ActionFunction() {
            public void eval(TaskContext context) {
                assertEquals(context.result().size(),1);
                counter[0]++;
                context.continueTask();
            }
        }).execute(graph,null);
        assertEquals(2, counter[0]);
        removeGraph();
    }


}