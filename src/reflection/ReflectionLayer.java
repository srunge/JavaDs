package reflection;

import java.util.HashMap;
import java.util.Map;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

public class ReflectionLayer
{
	private static Map<String, FieldTable> fieldTables = new HashMap<String, FieldTable>();
	private static Map<String, ObjectInstantiator<?>> objectInstantiators = new HashMap<String, ObjectInstantiator<?>>();
	
	public static FieldTable getFieldTable(Object object)
	{
		FieldTable fieldTable = fieldTables.get(object.getClass().getName());
		if (fieldTable == null)
		{
			fieldTable = new FieldTable(object.getClass());
			fieldTables.put(object.getClass().getName(), fieldTable);
		}
		return fieldTable;
	}
	
	public static ObjectInstantiator<?> getObjectInstantiator(String className)
	{
		ObjectInstantiator<?> instantiator = objectInstantiators.get(className);
		if (instantiator == null)
		{
			Objenesis objenesis = new ObjenesisStd();
			try
			{
				instantiator = objenesis.getInstantiatorOf(Class.forName(className));
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
				return null;
			}
			objectInstantiators.put(className, instantiator);
		}
		return instantiator;
	}
}
