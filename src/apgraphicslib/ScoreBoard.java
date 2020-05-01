package apgraphicslib;

public class ScoreBoard extends APLabel implements Updatable {
	
	private String score_phrase,end_phrase = "";
	private double score;
	private double scoreSpeed;
	private double targetScore;
	
	public int decimalsToRound = 1;
	
	public ScoreBoard(Object_draw drawer1, double x, double y, String score_phrase1, double score1) {
		super(drawer1,x,y);
		score_phrase = score_phrase1;
	}
	
	public void AddScore(double distance)
	{
		score = score + distance;
	}
	

	public ScoreBoard(Object_draw drawer1) {
		super(drawer1,0.05 * Settings.width,Settings.height-100);
		score_phrase = "Score:";
		score = 0;
	}
	
	@Override
	public void Update(double frames) {
		if (Math.abs(score - targetScore) > 0.01 * frames) {
			if (score < targetScore) {
				score += scoreSpeed * frames;
			}else {
				score -= scoreSpeed * frames;
			}		
		}
	}
	
	@Override
	public void prePaintUpdate() {
		if (decimalsToRound == 0) { 
			setMessage(score_phrase + " " + (int) Math.round(score) + end_phrase);
			
		}else {
			double scoreRoundMulti = Math.pow(10, decimalsToRound);
			setMessage(score_phrase + " " + Math.round(score * scoreRoundMulti)/scoreRoundMulti + end_phrase);
			
		}
			
	}
	
	
	public void setScore(double score1) {
		score = score1;
	}
	
	public void setScorePhrase(String score_phrase1) {
		score_phrase = score_phrase1;
	}
	
	public void setEndPhrase(String end_phrase1) {
		end_phrase = end_phrase1;
	}
	
	public void setTargetScore(double targetScore1) {
		targetScore = targetScore1;
	}
	
	public void setScoreSpeed(double scoreSpeed1) {
		scoreSpeed = scoreSpeed1;
	}
	

	public double getTargetScore() {
		return targetScore;
	}
	
}
