package change;

public class ValueChange extends Change
{
	private String attributeName;
	private Object oldValue;
	private Object newValue;

	public ValueChange(String targetObjectId, String attributeName, Object oldValue, Object newValue)
	{
		super(targetObjectId);
		this.attributeName = attributeName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	@Override
	public String toString()
	{
		return getTargetObjectId() + " " + attributeName + ": " + oldValue + " -> " + newValue;
	}

	public String getAttributeName()
	{
		return attributeName;
	}

	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}

	public Object getOldValue()
	{
		return oldValue;
	}

	public void setOldValue(Object oldValue)
	{
		this.oldValue = oldValue;
	}

	public Object getNewValue()
	{
		return newValue;
	}

	public void setNewValue(Object newValue)
	{
		this.newValue = newValue;
	}
}
