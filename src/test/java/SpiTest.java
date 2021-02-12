import org.junit.Test;
import top.misec.task.Task;

import java.util.ServiceLoader;

public class SpiTest {

    @Test
    public void allSpiTask() {
        ServiceLoader<Task> taskServiceLoader = ServiceLoader.load(Task.class);
        for (Task item : taskServiceLoader) {
            System.out.println("# " + item.getName());
            System.out.println(item.getClass().getName());
        }

    }
}
