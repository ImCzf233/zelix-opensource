package zelix.hack;

public class LOADTEST extends Hack
{
    public LOADTEST() {
        super("DontEnable!", HackCategory.ANOTHER);
    }
    
    @Override
    public void onEnable() {
        System.exit(0);
    }
}
