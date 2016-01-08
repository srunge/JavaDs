package diff;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import change.AssociationChange;
import change.CreateObjectChange;
import change.ValueChange;
import model.Model;
import reflection.FieldTable;
import reflection.ReflectionLayer;

public class DsDiffer extends Differ
{
	private Set<Object> alreadyDiffed = new HashSet<Object>();
	
	public DsDiffer(Model sourceModel, Model targetModel)
	{
		super(sourceModel, targetModel);
	}
	
	@Override
	public void diff() throws DiffException
	{
		Object sourceRoot = getSourceModel().getRoot();
		Object targetRoot = getTargetModel().getRoot();
		diffRecursive(sourceRoot, targetRoot);
	}
	
	private void diffRecursive(Object source, Object target) throws DiffException
	{
		if (target == null)
		{
			return;
		}
		
		if (alreadyDiffed.contains(target))
		{
			return;
		}
		
		alreadyDiffed.add(target);
		
		FieldTable fieldTable = ReflectionLayer.getFieldTable(target);
		
		diffPrimitiveFields(source, target, fieldTable);
		
		diffComplexFields(source, target, fieldTable);
	}

	private void diffPrimitiveFields(Object source, Object target, FieldTable fieldTable) throws DiffException
	{
		for (Field field : fieldTable.getPrimitiveFields())
		{
			Object sourceValue = null;
			Object targetValue = null;
			try
			{
				sourceValue = source != null ? field.get(source) : null;
				targetValue = target != null ? field.get(target) : null;
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				throw new DiffException("Illegal access to member " + field.getName() + ": " + e.getMessage());
			}
			boolean valueWasNull = sourceValue == null && targetValue != null;
			if (valueWasNull || !sourceValue.equals(targetValue)) // FIXME equals for float/double type?
			{
				String objectId = getTargetModel().getId(target);
				ValueChange valueChange = new ValueChange(objectId, field.getName(), sourceValue, targetValue);
				getChangeList().addChange(valueChange);
			}
		}
	}
	
	private void diffComplexFields(Object source, Object target, FieldTable fieldTable) throws DiffException
	{
		String objectId = getTargetModel().getId(target);
		
		for (Field field : fieldTable.getComplexFields())
		{
			Object sourceValue = null;
			Object targetValue = null;
			try
			{
				sourceValue = field.get(source);
				targetValue = field.get(target);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				throw new DiffException("Illegal access to member " + field.getName() + ": " + e.getMessage());
			}
			
			String sourceId = getSourceModel().getId(sourceValue);
			String targetId = getTargetModel().getId(targetValue);
			
			if (targetValue != null && targetId.isEmpty())
			{
				String newObjectId = getTargetModel().integrateObject(targetValue);
				CreateObjectChange change = new CreateObjectChange(objectId, field.getName(), newObjectId, targetValue.getClass().getName());
				getChangeList().addChange(change);
			}
			
			if (sourceValue == null && targetValue != null
					|| sourceValue != null && targetValue == null
					|| !sourceId.equals(targetId))
			{
				AssociationChange change = new AssociationChange(objectId, field.getName(), sourceId, getTargetModel().getId(targetValue));
				getChangeList().addChange(change);
			}

			diffRecursive(sourceValue, targetValue);
		}
	}
}
