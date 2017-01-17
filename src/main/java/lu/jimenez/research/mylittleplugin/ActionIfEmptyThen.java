package lu.jimenez.research.mylittleplugin;

import org.mwg.Callback;
import org.mwg.Constants;
import org.mwg.internal.task.CF_Action;
import org.mwg.internal.task.CoreTask;
import org.mwg.plugin.SchedulerAffinity;
import org.mwg.task.Task;
import org.mwg.task.TaskContext;
import org.mwg.task.TaskResult;

import java.util.Map;

public class ActionIfEmptyThen extends CF_Action {

    private org.mwg.task.Task _action;

    ActionIfEmptyThen(final Task action){
        super();
        this._action = action;
    }

    public Task[] children() {
        Task[] children_tasks = new Task[1];
        children_tasks[0] = _action;
        return children_tasks;
    }

    public void cf_serialize(StringBuilder builder, Map<Integer, Integer> dagIDS) {
        builder.append(MLPActionNames.IF_EMPTY_THEN);
        builder.append(Constants.TASK_PARAM_OPEN);
        final CoreTask castedAction = (CoreTask) _action;
        final int castedActionHash = castedAction.hashCode();
        if (dagIDS == null || !dagIDS.containsKey(castedActionHash)) {
            castedAction.serialize(builder, dagIDS);
        } else {
            builder.append("" + dagIDS.get(castedActionHash));
        }
        builder.append(Constants.TASK_PARAM_CLOSE);
    }

    public void eval(final TaskContext taskContext) {
        if(taskContext.result().size() == 0){
            _action.executeFrom(taskContext, taskContext.result(), SchedulerAffinity.SAME_THREAD, new Callback<TaskResult>() {
                public void on(TaskResult res) {
                    taskContext.continueWith(res);
                }
            });
        }else taskContext.continueTask();
    }

}
