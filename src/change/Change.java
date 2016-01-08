package change;

public abstract class Change
{
	private String targetObjectId;
	
	public Change(String targetObjectId)
	{
		this.targetObjectId = targetObjectId;
	}

	public String getTargetObjectId()
	{
		return targetObjectId;
	}

	public void setTargetObjectId(String targetObjectId)
	{
		this.targetObjectId = targetObjectId;
	}
}
