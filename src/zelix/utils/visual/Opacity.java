package zelix.utils.visual;

public class Opacity
{
    private float opacity;
    private long lastMS;
    
    public Opacity(final int opacity) {
        this.opacity = opacity;
        this.lastMS = System.currentTimeMillis();
    }
    
    public void interpolate(final int targetOpacity) {
        this.opacity = (int)AnimationUtil.calculateCompensation(targetOpacity, this.opacity, 16L, 5.0);
    }
    
    public void interp(final float targetOpacity, final double speed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.opacity = AnimationUtil.calculateCompensation(targetOpacity, this.opacity, delta, speed);
    }
    
    public float getOpacity() {
        return this.opacity;
    }
    
    public void setOpacity(final float opacity) {
        this.opacity = opacity;
    }
}
