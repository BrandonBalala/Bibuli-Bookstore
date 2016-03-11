package com.g4w16.beans;

/**
 *
 * @author Annie So
 */
public class SurveyBean {

    private int id;
    private String question;
    private String answer1;
    private int timesAnswered1;
    private String answer2;
    private int timesAnswered2;
    private String answer3;
    private int timesAnswered3;
    private String answer4;
    private int timesAnswered4;

    /**
     * Default constructor for creating a survey bean.
     */
    public SurveyBean() {
        this(0, "", "", 0, "", 0, "", 0, "", 0);
    }

    /**
     * Constructor for creating a new survey bean and setting each field of the
     * bean.
     * 
     * @param id The id of the survey.
     * @param question The survey question.
     * @param answer1 The first answer.
     * @param timesAnswered1 The number of times the first answer was chosen.
     * @param answer2 The second answer.
     * @param timesAnswered2 The number of times the second answer was chosen.
     * @param answer3 The third answer.
     * @param timesAnswered3 The number of times the third answer was chosen.
     * @param answer4 The fourth answer.
     * @param timesAnswered4 The number of times the fourth answer was chosen.
     */
    public SurveyBean(final int id, final String question, final String answer1, 
            final int timesAnswered1, final String answer2, final int timesAnswered2, 
            final String answer3, final int timesAnswered3, final String answer4, 
            final int timesAnswered4) {
        super();
        this.id = id;
        this.question = question;
        this.answer1 = answer1;
        this.timesAnswered1 = timesAnswered1;
        this.answer2 = answer2;
        this.timesAnswered2 = timesAnswered2;
        this.answer3 = answer3;
        this.timesAnswered3 = timesAnswered3;
        this.answer4 = answer4;
        this.timesAnswered4 = timesAnswered4;
    }

    /**
     * Gets the id.
     *
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id The id to set.
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Gets the question.
     *
     * @return The question.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the question.
     *
     * @param question The question to set.
     */
    public void setQuestion(final String question) {
        this.question = question;
    }

    /**
     * Gets the first answer.
     *
     * @return The first answer.
     */
    public String getAnswer1() {
        return answer1;
    }

    /**
     * Sets the first answer.
     *
     * @param answer1 The first answer to set.
     */
    public void setAnswer1(final String answer1) {
        this.answer1 = answer1;
    }

    /**
     * Gets the number of times the first answer was chosen.
     *
     * @return The number of times the first answer was chosen.
     */
    public int getTimesAnswered1() {
        return timesAnswered1;
    }

    /**
     * Sets the number of times the first answer was chosen.
     *
     * @param timesAnswered1 The number of times the first answer was chosen to
     * set.
     */
    public void setTimesAnswered1(final int timesAnswered1) {
        this.timesAnswered1 = timesAnswered1;
    }

    /**
     * Gets the second answer.
     *
     * @return The second answer.
     */
    public String getAnswer2() {
        return answer2;
    }

    /**
     * Sets the second answer.
     *
     * @param answer2 The second answer to set.
     */
    public void setAnswer2(final String answer2) {
        this.answer2 = answer2;
    }

    /**
     * Gets the number of times the second answer was chosen.
     *
     * @return The number of times the second answer was chosen.
     */
    public int getTimesAnswered2() {
        return timesAnswered2;
    }

    /**
     * Sets the number of times the second answer was chosen.
     *
     * @param timesAnswered2 The number of times the second answer was chosen to
     * set.
     */
    public void setTimesAnswered2(final int timesAnswered2) {
        this.timesAnswered2 = timesAnswered2;
    }

    /**
     * Gets the third answer.
     *
     * @return The third answer.
     */
    public String getAnswer3() {
        return answer3;
    }

    /**
     * Sets the third answer.
     *
     * @param answer3 The third answer to set.
     */
    public void setAnswer3(final String answer3) {
        this.answer3 = answer3;
    }

    /**
     * Gets the number of times the third answer was chosen.
     *
     * @return The number of times the third answer was chosen.
     */
    public int getTimesAnswered3() {
        return timesAnswered3;
    }

    /**
     * Sets the number of times the third answer was chosen.
     *
     * @param timesAnswered3 The number of times the third answer was chosen to 
     * set.
     */
    public void setTimesAnswered3(final int timesAnswered3) {
        this.timesAnswered3 = timesAnswered3;
    }

    /**
     * Gets the fourth answer.
     *
     * @return The fourth answer.
     */
    public String getAnswer4() {
        return answer4;
    }

    /**
     * Sets the fourth answer.
     *
     * @param answer4 The fourth answer to set.
     */
    public void setAnswer4(final String answer4) {
        this.answer4 = answer4;
    }

    /**
     * Gets the number of times the fourth answer was chosen.
     *
     * @return The number of times the fourth answer was chosen.
     */
    public int getTimesAnswered4() {
        return timesAnswered4;
    }

    /**
     * Sets the number of times the fourth answer was chosen.
     *
     * @param timesAnswered4 The number of times the fourth answer was chosen to
     * set.
     */
    public void setTimesAnswered4(final int timesAnswered4) {
        this.timesAnswered4 = timesAnswered4;
    }
}
