import java.io.*;
import javax.media.j3d.*;

class MyAlpha extends Alpha
{
	public MyAlpha(int loopCount, long increasingAlphaDuration)
	{
		super(loopCount, increasingAlphaDuration);
	}

	public float value()
	{
		return (float)Math.sqrt(super.value());
	}

	public void setStartTime(long startTime)
	{
		super.setStartTime(startTime);
	}

	public void setTriggerTime(long triggerTime)
	{
		super.setTriggerTime(triggerTime);
	}
}