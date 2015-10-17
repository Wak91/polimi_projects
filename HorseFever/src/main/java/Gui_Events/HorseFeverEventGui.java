package Gui_Events;

import java.io.Serializable;

import View.GUI_interface;

public abstract class HorseFeverEventGui implements Serializable {

	protected GUI_interface gui_interface;
	private Class tipo;
	
	
	public HorseFeverEventGui(Class tipo)
	{
		this.tipo = tipo;
	}
	
	public GUI_interface get_gui() {
		return this.gui_interface;
		
	}
	
	public Class getTipoEvento()
	{return this.tipo;}

}
