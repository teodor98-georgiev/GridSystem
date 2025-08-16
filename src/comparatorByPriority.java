import java.util.Comparator;

public class comparatorByPriority implements Comparator<MantainanceTask> {
    @Override
    public int compare(MantainanceTask o1, MantainanceTask o2) {
        return Integer.compare(o1.getPriority(),o2.getPriority());
    }
}
