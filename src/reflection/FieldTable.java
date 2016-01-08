package reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldTable
{
	private Map<String, Field> allFields = new HashMap<String, Field>();
	private List<Field> primitiveFields = new ArrayList<Field>();
	private List<Field> complexFields = new ArrayList<Field>();
	private List<Field> collectionFields = new ArrayList<Field>();
	
	// TODO static fields
	
	public FieldTable(Class<?> clazz)
	{
		for (Field field : clazz.getDeclaredFields())
		{
			field.setAccessible(true);
			allFields.put(field.getName(), field);
			if (field.getType().isPrimitive() || field.getType().equals(String.class))
			{
				primitiveFields.add(field);
			}
			else if (field.getType().isArray() || Collection.class.isAssignableFrom(field.getType()))
			{
				collectionFields.add(field);
			}
			else
			{
				complexFields.add(field);
			}
		}
	}
	
	public Field getField(String name)
	{
		return allFields.get(name);
	}
	
	public Collection<Field> getAllFields()
	{
		return allFields.values();
	}
	
	public List<Field> getPrimitiveFields()
	{
		return primitiveFields;
	}
	
	public List<Field> getComplexFields()
	{
		return complexFields;
	}
	
	public List<Field> getCollectionFields()
	{
		return collectionFields;
	}
}
