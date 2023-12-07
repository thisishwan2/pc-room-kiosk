package org.example;

import javax.swing.*;
import java.awt.*;

public class PcRoomFoodKiosk extends JFrame {

    public class NamePanel extends JPanel {
        JLabel title;
        JLabel imageLabel;

        public NamePanel(){
            // 배치관리자
            setLayout(new BorderLayout());
            setBackground(new Color(0xFF8E1F));

            // 이미지 추가
            ImageIcon imageIcon = new ImageIcon("/Users/ran/Desktop/동국대학교/3학년_2학기/융프2/과제/기말_키오스크/pc-room-kiosk/img/동국대로고.png");
            Image image = imageIcon.getImage();
            Image newImg = image.getScaledInstance(230, 100, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(newImg);
            imageLabel = new JLabel(imageIcon);

            // 라벨 및 폰트
            title = new JLabel("PC방 음식 키오스크");
            Font font = new Font("맑은 고딕", Font.BOLD, 30);
            title.setFont(font);

            // 가운데 정렬
            title.setHorizontalAlignment(JLabel.CENTER);
            add("West", imageLabel);
            add("Center", title);

        }
    }

    // 패널 생성
    private JPanel namePanel = new NamePanel(); // 가게명 패널
    private JPanel tabPanel = new TabPanel(); // 탭 패널(탭 화면)
    private JPanel orderPanel = new OrderPanel(this); // 장바구니, 결제하기 버튼

    public PcRoomFoodKiosk(){

        setTitle("PC방 키오스크");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 배치관리자
        // 그리드백 객체
        GridBagLayout gridbag = new GridBagLayout();

        // 그리드백 레이아웃에 포함된 컴포넌트 속성 제어 객체
        GridBagConstraints constraint = new GridBagConstraints();
        setLayout (gridbag);

        // 컴포넌트를 그리드 셀에 맞도록 채워넣음
        constraint.fill = GridBagConstraints.BOTH;
        //Component 크기를 비율로 지정
        // 1이상시 가로 세로로 더 많은 공간 차지 -> 창 크기 변경시 자동 확장
        constraint.weightx = 1;
        constraint.weighty = 1;


            // frame에 panel 붙이기
            constraint.gridwidth = GridBagConstraints.REMAINDER; // 현재 컴포넌트가 현재 행에서 나머지 모든 칸을 차지하도록 합니다.
            constraint.weighty = 0.2; // namePanel 높이 크기 설정
            gridbag.setConstraints(namePanel, constraint);
            add(namePanel);

            // tabPanel 의 크기 조정
            constraint.gridwidth = 1; // 넓이 1칸 차지
            constraint.weighty = 1; // 높이 비율
            constraint.weightx = 0.7; // 넓이 비율
            gridbag.setConstraints(tabPanel, constraint);
            add(tabPanel);

            // orderPanel의 크기 조정
            constraint.weightx = 0.3; // 넓이 비율
            gridbag.setConstraints(orderPanel, constraint);
            add(orderPanel);

        setSize (1200, 800);
        setVisible (true);

    }

    public static void main(String[] args) {
        new PcRoomFoodKiosk();
    }
}
