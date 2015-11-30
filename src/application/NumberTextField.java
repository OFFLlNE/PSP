package application;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField
{
	public int range_min;
	public int range_max;
	
    public NumberTextField(int range_min, int range_max) {
		super();
		this.range_min = range_min;
		this.range_max = range_max;
		
	}
    
    public NumberTextField() {
		super();
	}
    
	@Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
        
        
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
        
    }

    private boolean validate(String text)
    {
    	return ("".equals(text) || text.matches("[0-9]"));
    	
    }
    
    
   
    
}