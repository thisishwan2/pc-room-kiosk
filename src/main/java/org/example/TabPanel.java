package org.example;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;

// 메뉴를 고르는 키오스크 탭 화면
public class TabPanel extends JPanel {

    private JSlider[] sl = new JSlider[3];
    private JLabel colorLabel;


    // 탭 화면으로 구성되는 탭 팬
    private JTabbedPane tp;
    // 6개의 탭 메뉴들의 이름
    private static String[] tabTitle = {"과자", "분식", "라면", "식사", "음료", "etc"};
    // 각각의 탭 화면을 구성하는 JPanel
    private JPanel[] menuPanel = new JPanel[tabTitle.length];
    // 메뉴 버튼 title
    public static String[][] menuTitle = new String[tabTitle.length][];
    // 메뉴 버튼 가격
    public static String[][] menuPrice = new String[tabTitle.length][];
    // 메뉴 버튼
    private JButton[][] menuButton = new JButton[tabTitle.length][];
    // 버튼 이미지 경로

    private String imgPath = "/Users/ran/Desktop/동국대학교/3학년_2학기/융프2/과제/기말_키오스크/pc-room-kiosk/img/";
    // 이미지 파일명
    private String[][] imgName = new String[tabTitle.length][];

    public ImageIcon transformImageSize(ImageIcon imageIcon){
        Image image = imageIcon.getImage();
        Image scaledInstance = image.getScaledInstance(180, 150, Image.SCALE_SMOOTH);
        ImageIcon newImageIcon = new ImageIcon(scaledInstance);
        return newImageIcon;
    }

    public TabPanel() {
        setLayout(new BorderLayout());
        Font font = new Font("맑은 고딕", Font.PLAIN, 20);
        UIManager.put("TabbedPane.selected", new Color(0xEED0CE));
        tp = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tp.setFont(font);

        // 초기화를 여기서 진행
        this.colorLabel = new JLabel("SLIDER EXAMPLE");

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.weightx = 1;
        constraint.weighty = 1;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;

        for (int i = 0; i < tabTitle.length; i++) {
            menuPanel[i] = new JPanel();
            menuPanel[i].setLayout(gridbag);
            menuPanel[i].setBackground(new Color(0xE9E9E2));
        }

        menuTitle[0] = new String[]{"새우깡", "감자깡", "포스틱", "양파링", "꼬깔콘", "초코파이", "초코송이", "초코칩쿠키", "허니버터칩", "프링글스", "포카칩", "꼬북칩"};
        menuTitle[1] = new String [] {"떡볶이", "치즈스틱", "순대", "오징어튀김", "김말이", "피카츄", "닭꼬치"};
        menuTitle[2] = new String [] {"진라면", "열라면", "신라면", "참깨라면", "삼양라면", "짜파게티", "안성탕면","불닭볶음면","틈새라면"};
        menuTitle[3] = new String [] {"볶음밥", "만두","핫도그","치킨마요덮밥", "김치볶음밥", "오므라이스", "햄버거", "토스트", "샌드위치"};
        menuTitle[4] = new String [] {"콜라", "제로콜라", "사이다", "몬스터", "핫식스", "아이스티"};

        // 메뉴 버튼 가격
        menuPrice[0] = new String [] {"1500", "1600", "1400", "1500", "1500", "1200", "1600", "1700", "1100", "1500", "1200", "1240", "1500"};
        menuPrice[1] = new String [] {"3000", "1700", "2500", "1200", "1200", "1000", "1500"};
        menuPrice[2] = new String [] {"2200", "2000", "2300", "2000", "2400", "1800", "3500","1800","2000","2100"};
        menuPrice[3] = new String [] {"4500", "1500", "2500", "5000", "5000", "5000", "3200", "2800", "2000"};
        menuPrice[4] = new String [] {"2000", "2000", "2000", "2000", "3000", "2500", "1500"};

        // 메뉴 이미지 파일명
        for (int i = 0; i < tabTitle.length-1; i++) {
            imgName[i] = new String[menuTitle[i].length];
            for (int j = 0; j < menuTitle[i].length; j++) {
                imgName[i][j] = menuTitle[i][j] + ".jpeg";
                System.out.println("Image Path: " + imgName[i][j]);
            }
        }



        // 메뉴버튼 생성
        for(int i=0; i < menuTitle.length-1; i++){
            menuButton[i] = new JButton[menuTitle[i].length];
            for(int j=0; j < menuTitle[i].length; j++){
                // 버튼 이름
                String str = "<html>" + menuTitle[i][j] + "<br>" + menuPrice[i][j] + "원</html>";
//                    menuButton[i][j] = new JButton(menuTitle[i][j]);
                menuButton[i][j] = new JButton(str);
                menuButton[i][j].setVerticalTextPosition(JButton.BOTTOM);  // 텍스트 아래로
                menuButton[i][j].setHorizontalTextPosition(JButton.CENTER); // 텍스트 가운데
                menuButton[i][j].setFont(font);
                // 버튼 이미지
                menuButton[i][j].setIcon(transformImageSize(new ImageIcon(imgPath + imgName[i][j]))); // 버튼 이미지
                menuButton[i][j].setPreferredSize(new Dimension(200, 200));
                // 버튼 배경색, 테두리
                menuButton[i][j].setBackground(new Color(0xFFFFFF));
                menuButton[i][j].setFocusPainted(false);
                menuButton[i][j].setBorderPainted(false);


                menuButton[i][j].addActionListener(new MenuBtnActionListener());
            }
        }




        // 각 메뉴판 패널에 메뉴 버튼 붙이기
        for (int i = 0; i < menuPanel.length-1; i++) {
            int gridx = 0;
            int gridy = 0;
            for (int j = 0; j < menuButton[i].length; j++) {
                constraint.gridx = gridx;
                constraint.gridy = gridy;
                // 메뉴 버튼을 각 행에 2개씩 배치
                gridx++;
                if (gridx > 1) {
                    gridx = 0;
                    gridy++;
                }
                gridbag.setConstraints(menuButton[i][j], constraint);
                menuPanel[i].add(menuButton[i][j]);
            }
        }

        for (int i = 0; i < tabTitle.length; i++) {
            JScrollPane scrollPane = new JScrollPane(menuPanel[i]);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            tp.addTab("<html><body><table width='94'>" + tabTitle[i] + "</table></body></html>", scrollPane);
        }


        // 탭 패널에 탭 팬 붙이기
        add("Center", tp);

        // Add a change listener to the "etc" tab
        tp.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tp.getSelectedIndex();
                if (selectedIndex == tabTitle.length - 1) { // Check if "etc" tab is selected
                    openColorSliderFrame();
                }
            }
        });

    }

    private void openColorSliderFrame() {
        for (int i = 0; i < sl.length; i++) {
            sl[i] = new JSlider(JSlider.HORIZONTAL, 0, 255, 128);
            sl[i].setPaintLabels(true);
            sl[i].setPaintTicks(true);
            sl[i].setPaintTrack(true);
            sl[i].setMajorTickSpacing(50);
            sl[i].setMinorTickSpacing(10);
            sl[i].addChangeListener(new MyChangeListener());
            menuPanel[tabTitle.length - 1].add(sl[i]);  // "etc" 탭의 menuPanel에 슬라이더 추가
        }

        sl[0].setForeground(Color.RED);
        sl[1].setForeground(Color.GREEN);
        sl[2].setForeground(Color.BLUE);

        int r = sl[0].getValue();
        int g = sl[1].getValue();
        int b = sl[2].getValue();

        // 로컬 변수 대신 인스턴스 변수에 액세스합니다.
        this.colorLabel.setOpaque(true);
        this.colorLabel.setBackground(new Color(r, g, b));
        menuPanel[tabTitle.length - 1].add(this.colorLabel);  // "etc" 탭의 menuPanel에 colorLabel 추가
        menuPanel[tabTitle.length - 1].revalidate();  // 변경 사항을 반영하기 위해 패널을 새로 고칩니다
        menuPanel[tabTitle.length - 1].repaint();
    }

    class MyChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            int r = sl[0].getValue();
            int g = sl[1].getValue();
            int b = sl[2].getValue();
            colorLabel.setBackground(new Color(r, g, b));
        }
    }

//    class MyActionListner implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//            JButton btn = (JButton) e.getSource();
//            String b = btn.getText();
//            System.out.println(b);
//        }
//    }

    // 메뉴 버튼 리스너
    class MenuBtnActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            String b = btn.getText();

            // 버튼으로 눌려진 메뉴명
            b = b.split(">")[1];
            b = b.split("<")[0];

            // 장바구니에 담긴 서로 다른 메뉴의 개수
            int tableSize = OrderPanel.model.getRowCount();
            // 장바구니에 담긴 해당 메뉴의 수량
            int quantity = 0;
            // 해당 메뉴의 가격
            int price = 0;
            // 이미 장바구니에 담겨져 있는 검사
            boolean inBasket = false;

            // 해당 메뉴 가격
            for(int i=0; i < menuTitle.length-1; i++)
                for(int j=0; j < menuTitle[i].length; j++)
                    if(b.equals(menuTitle[i][j]))
                        price = Integer.parseInt(menuPrice[i][j]);


            // 장바구니에 이미 담겨있는지 검사
            for(int i=0; i < tableSize; i++){
                // 같은 값이 존재할 때
                if(b.equals(OrderPanel.model.getValueAt(i,0))) {
                    String[] curMenu = new String[2];
                    curMenu[0] = (String) OrderPanel.model.getValueAt(i,1); // 수량
                    curMenu[1] = (String) OrderPanel.model.getValueAt(i,2); // 가격

                    quantity = Integer.parseInt(curMenu[0]) + 1; // 수량 + 1
                    price = quantity * price ; // 수량 * 가격

                    // table 업데이트
                    OrderPanel.model.setValueAt(Integer.toString(quantity),i,1);
                    OrderPanel.model.setValueAt(Integer.toString(price),i,2);

                    inBasket = true;
                    break;
                }

            }
            // 새로운 상품이라면
            if(!inBasket){
                tableSize++; // 테이블 사이즈 + 1

                String[] newMenu = new String[3];
                newMenu[0] = b;
                newMenu[1] = "1";
                newMenu[2] = Integer.toString(price);


                // table 업데이트
                OrderPanel.model.addRow(newMenu);
            }

            // 합계 업데이트
            int sum = 0;
            for(int i=0; i < tableSize; i++)
                sum += Integer.parseInt((String) OrderPanel.model.getValueAt(i,2));

            OrderPanel.sumPrice.setText(Integer.toString(sum) + "원");

        }
    }
}