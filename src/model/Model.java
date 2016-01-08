package model;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objenesis.instantiator.ObjectInstantiator;

import change.AssociationChange;
import change.Change;
import change.ChangeList;
import change.CreateObjectChange;
import change.ValueChange;
import reflection.ReflectionLayer;

public class Model
{
	private Object root;
	private Map<String, Object> idToObject = new HashMap<String, Object>();
	private Map<Object, String> objectToId = new HashMap<Object, String>();
	private int currentIdNumber = 0;

	public Model(Object root)
	{
		this.root = root;
		integrateObject(root);
	}

	public Object getRoot()
	{
		return root;
	}

	public String getId(Object object)
	{
		String id = objectToId.get(object);
		return id != null ? id : "";
	}
	
	public boolean apply(ChangeList changeList)
	{
		boolean success = false;
		List<Change> successfullyApplied = new ArrayList<Change>();
		for (Change change : changeList.getChanges())
		{
			success = apply(change);
			if (success)
			{
				successfullyApplied.add(change);
			}
			else
			{
				break;
			}
		}
		
		if (!success)
		{
			// TODO revert successfully applied changes
			
			return false;
		}
		
		return true;
	}
	
	private boolean apply(Change change)
	{
		if (change instanceof ValueChange)
		{
			return applyValueChange((ValueChange) change);
		}
		if (change instanceof AssociationChange)
		{
			return applyAssociationChange((AssociationChange) change);
		}
		if (change instanceof CreateObjectChange)
		{
			return applyCreateObjectChange((CreateObjectChange) change);
		}
		return false;
	}
	
	private boolean applyValueChange(ValueChange change)
	{
		Object targetObject = idToObject.get(change.getTargetObjectId());
		if (targetObject == null)
		{
			return false;
		}
		Field field = ReflectionLayer.getFieldTable(targetObject).getField(change.getAttributeName());
		if (field == null)
		{
			return false;
		}
		try
		{
			field.set(targetObject, change.getNewValue());
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean applyAssociationChange(AssociationChange change)
	{
		Object targetObject = idToObject.get(change.getTargetObjectId());
		if (targetObject == null)
		{
			return false;
		}
		Field field = ReflectionLayer.getFieldTable(targetObject).getField(change.getAttributeName());
		if (field == null)
		{
			return false;
		}
		Object newValue = idToObject.get(change.getNewObjectId());
		try
		{
			field.set(targetObject, newValue);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean applyCreateObjectChange(CreateObjectChange change)
	{
		Object targetObject = idToObject.get(change.getTargetObjectId());
		if (targetObject == null)
		{
			return false;
		}
		Field field = ReflectionLayer.getFieldTable(targetObject).getField(change.getAttributeName());
		if (field == null)
		{
			return false;
		}
		ObjectInstantiator<?> instantiator = ReflectionLayer.getObjectInstantiator(change.getClassName());
		if (instantiator == null)
		{
			return false;
		}
		Object newObject = instantiator.newInstance();
		if (newObject == null)
		{
			return false;
		}
		integrateObject(newObject, change.getNewObjectId());
		try
		{
			field.set(targetObject, newObject);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String integrateObject(Object object)
	{
		String id = generateNewId();
		integrateObject(object, id);
		return id;
	}
	
	public void integrateObject(Object object, String id)
	{
		idToObject.put(id, object);
		objectToId.put(object, id);
	}
	
	private String generateNewId()
	{
		currentIdNumber++;
		return "aaa." + currentIdNumber;
	}
}
