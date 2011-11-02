package cn.gamemate.app.domain.event.dota;


public interface DotaRepProcessor {
	
	@SuppressWarnings("serial")
	class ProcessingException extends Exception{

		public ProcessingException(String string) {
			super(string);
		}
		
	}
	
	void postProcess(DotaRepInfo repInfo) throws ProcessingException;
	
	String getDescription();
}
