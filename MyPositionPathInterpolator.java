import java.awt.*;
import java.io.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class MyPositionPathInterpolator extends PositionPathInterpolator
{
	float right = 0.7f;
	float left = -0.7f;
	float top = 0.5f;
	float bottom = -0.5f;
	float twid = 2*(right-left);
	float thei = 2*(top-bottom);

	public MyPositionPathInterpolator(Alpha alpha, TransformGroup target, Transform3D axisOfTransform, float[] knots, Point3f[] positions)
	{
		super(alpha, target, axisOfTransform, knots, positions);
	}

	public void setPosition(int index, Point3f position)
	{
		super.setPosition(index, position);
	}

	public void computeTransform(float a, Transform3D tran)
	{
		super.computeTransform(a, tran);
		float[] array = new float[16];
		tran.get(array);

		float x = (array[3] - left)/twid;
		x = x - (float)Math.floor(x);
		if(x > 0.5f)
		{
			x = 1 - x;
		}
		array[3] = x*twid + left;

		float y = (array[7] - bottom)/thei;
		y = y - (float)Math.floor(y);
		if(y > 0.5f)
		{
			y = 1 - y;
		}
		array[7] = y*thei + bottom;

		tran.set(array);
	}
}