package otus.spring.homework.service.examination;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import otus.spring.homework.model.ExamResult;
import otus.spring.homework.model.ExaminationTicketAnswer;

import java.util.Collection;

@Component
public class ExamResultEvaluationImpl implements ExamResultEvaluation {
    private final int numberOfCorrectAnswersToPass;

    private final int maxScore;

    public ExamResultEvaluationImpl(@Value("${number-of-correct-answers-to-pass}") int numberOfCorrectAnswersToPass,
                                    @Value("${max-score}") int maxScore) {
        this.numberOfCorrectAnswersToPass = numberOfCorrectAnswersToPass;
        this.maxScore = maxScore;
    }

    @Override
    public boolean examPassed(ExamResult examResult) {
        var answers = examResult.getAnswers();
        if (answers.size() < numberOfCorrectAnswersToPass) {
            throw new IllegalStateException("The number of correct answers to pass " +
                    "must be less than or equal to the size of the questions");
        }
        return getCorrectAnswersCount(answers) >= numberOfCorrectAnswersToPass;
    }

    @Override
    public int getExaminationScore(ExamResult examResult) {
        var answers = examResult.getAnswers();
        return maxScore * getCorrectAnswersCount(answers) / answers.size();
    }

    @Override
    public int getMaxScore() {
        return maxScore;
    }

    private int getCorrectAnswersCount(Collection<ExaminationTicketAnswer> answers) {
        return (int) (answers.stream().filter(ExaminationTicketAnswer::isCorrect).count());
    }
}
