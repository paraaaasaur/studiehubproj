class Answer {
    A = false;
    B = false;
    C = false;
    D = false;
    E = false;

    static parseQ_answer(q_answer = '', separator = ',') {
        const instance = new Answer();
        q_answer.split(separator).forEach(label => instance[label] = true);

        return instance;
    }

    static equals(answerA, answerB) {
        return Object.keys(answerA).every(k => answerA[k] === answerB[k])
            && Object.keys(answerB).every(k => answerA[k] === answerB[k]);
    }

    resetToAllFalse() {
        Object.keys(this).forEach(k => this[k] = false);
    }

    /** Return an ascending answer string like 'ABE' */
    getChosenLabels() {
        return Object.keys(this)
            .filter(key => this[key] === true)
            .sort()
            .reduce((last, here) => last + here, '')
    }

    /** Caution: Based on the assumption that not choosing any selection is not a valid answer.*/
    isEmpty() {
        return !Object.values(this).includes(true);
    }
}

class Selections {
    constructor({q_selectionA, q_selectionB, q_selectionC, q_selectionD, q_selectionE}) {
        this.A = q_selectionA;
        this.B = q_selectionB;
        this.C = q_selectionC;
        this.D = q_selectionD;
        this.E = q_selectionE;

        // remove absent selection
        for (const key of Object.keys(this)) {
            if (!this[key]) {
                delete this[key];
            }
        }

        // selections are immutable
        Object.freeze(this);
    }
}

class QuestionInfo {
    q_id;
    q_class;
    q_type;
    q_question;
    selections;
    correctAnswer;
    verification;
    createDate;
    q_pictureString;
    q_audioString;

    constructor() {}

    static fromResObj(resObj) {
        const instance = new QuestionInfo();

        instance.q_id = resObj.q_id;
        instance.q_class = resObj.q_class;
        instance.q_type = resObj.q_type;
        instance.q_question = resObj.q_question;
        instance.verification = resObj.verification;
        instance.createDate = resObj.createDate;
        instance.q_pictureString = resObj.q_pictureString;
        instance.q_audioString = resObj.q_audioString;

        instance.selections = new Selections(resObj);
        instance.correctAnswer = Answer.parseQ_answer(resObj.q_answer, ',');

        return instance;
    }
}