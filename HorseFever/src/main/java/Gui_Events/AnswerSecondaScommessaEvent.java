package Gui_Events;

public class AnswerSecondaScommessaEvent extends HorseFeverEventGui {

	boolean ok_answer;
	public AnswerSecondaScommessaEvent(boolean answer) {
		super(AnswerSecondaScommessaEvent.class);
		ok_answer=answer;
		
	}
	public boolean isOk_answer() {
		return ok_answer;
	}

}
