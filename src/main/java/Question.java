public class Question {
    String type ;
    String text;
    String answer;
   // type answer with text // dropdown // checkbox
    public  Question() {

    }

    public  Question(String type, String text,String answer) {
        this.text = text;
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public String getAnswer() {
        return answer;
    }

    public String getText() {
        return text;
    }
}
