package client;

import controller.Executable;
import controller.Loop;
import hibernate.Connector;
import view.MyFrame;
import view.panel.LoginPanel;
import view.panel.PanelType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    private static final Client instance = new Client();
    private final JFrame frame;
    private final Map<PanelType,JPanel> panels;
    private Connector connector;
    private final List<Executable> tempAnswerList,answerList;
    private final Loop executor;

    public Client() {
        this.frame = new MyFrame();
        panels= new HashMap<>();
        panels.put(PanelType.LOGIN_PANEL,new  LoginPanel());
        tempAnswerList = new ArrayList<>();
        answerList =new ArrayList<>();
        executor =new Loop(60,this::executeAnswers);
    }

    public static Client getInstance() {
        return instance;
    }

    private void executeAnswers() {
        answerList.forEach(Executable::execute);
        answerList.clear();
        synchronized (tempAnswerList) {
            answerList.addAll(tempAnswerList);
            tempAnswerList.clear();
        }
    }

    public void putAnswer(Answer answer) {
        if (answer != null)
            synchronized (tempAnswerList) {
                tempAnswerList.add(answer);
            }
    }

}
