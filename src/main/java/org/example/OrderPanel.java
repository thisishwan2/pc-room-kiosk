package org.example;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderPanel extends JPanel {

    // 다이얼로그 띄울 때 ParentComponent 로 사용
    private JFrame f;

    // 1. 장바구니 테이블, 합계 패널
    private JPanel basketPanel; // scrollPane, sumPanel이 붙을 패널
    private JScrollPane scrollPane; // 테이블 스크롤 가능
    private JTable table; // 장바구니 테이블
    private String header[] = {"상품명", "수량", "가격"}; // 테이블 헤더
    public static DefaultTableModel model; // 테이블 관리를 위한 모델 선언
    private int row = -1; // 테이블 클릭시 선택된 행
    private static JPanel sumPanel; // 합계 패널
    public static JLabel sumPrice; // 합계

    // 2. 장바구니 수정 버튼이 붙여진 패널
    private JPanel editBtnsPanel; // editBtns가 붙을 패널
    private String[] btn3Title = {"수량 감소", "품목 삭제", "초기화"}; // 버튼명
    private JButton[] editBtns; // 수량 감소, 선택한 메뉴 삭제, 테이블 초기화 버튼

    // 3. 결제하기 버튼, 결제 화면 패널
    private JPanel payPanel; // timerLabel, choicePanel, payBtn이 붙을 패널
    private Thread timerTh; // 타이머 스레드 -> 멀티 스레딩으로 이벤트를 별도로 동작시키기 위해
    private JLabel timerLabel; // 타이머가 표시될 라벨
//    private TimerRunnable runnable; // 스레드의 run() 구현
    private JPanel choicePanel; // 결제 화면
    private JButton ok; // 결제 확인 버튼
    private JButton cancel; // 결제 취소 버튼
    private JButton payBtn; // 결제하기 버튼

    // MySQL 연결 정보
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/pcRoomKiosk?characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "ch122411";


    public OrderPanel(JFrame f){
        this.f = f;
        // 배치관리자
        setLayout(new BorderLayout());
        // 폰트
        Font font = new Font("맑은 고딕", Font.PLAIN, 23);

        // 1. 장바구니 패널 - 테이블, 합계
        // 장바구니 패널 생성, 배치관리자 설정
        basketPanel = new JPanel();
        basketPanel.setLayout(new BorderLayout());

        // 테이블 생성, 마우스 리스너 붙이기
        model = new DefaultTableModel(header, 0);
        table = new JTable(model);
        table.addMouseListener(new TableMouseListener());
        scrollPane = new JScrollPane(table);

        // 테이블에 폰트 적용
        table.getTableHeader().setFont(font); // 헤더
        table.setFont(font); // 셀
        table.setRowHeight(30);

        // 합계 패널
        font = new Font("맑은 고딕", Font.BOLD, 30);
        sumPanel = new JPanel();
        sumPrice = new JLabel("0원");
        sumPrice.setFont(font);
        JLabel str = new JLabel("합계");
        str.setFont(font);
        sumPanel.add(str);
        sumPanel.add(sumPrice);

        // 장바구니 패널에 테이블, 합계 컴포넌트 추가
        basketPanel.add("North", scrollPane);
        basketPanel.add("Center", sumPanel);


        // 2. 테이블 수정 버튼 패널 - 줄이기, 삭제, 초기화 버튼
        // 수정 버튼 패널 생성
        editBtnsPanel = new JPanel();

        // 줄이기, 삭제, 초기화 버튼
        font = new Font("Arial", Font.BOLD, 17);
        editBtns = new JButton[3];
        for(int i=0; i < editBtns.length; i++){
            editBtns[i] = new JButton(btn3Title[i]);
            editBtns[i].setFont(font);
            editBtns[i].setBackground(new Color(0xEED0CE));
            // 각 버튼에 액션 리스너 붙이기
            editBtns[i].addActionListener(new EditBtnsActionListener());
            // 수정 버튼 패널에 버튼 붙이기
            editBtnsPanel.add(editBtns[i]);
        }


        // 결제하기 패널 - 결제 화면, 결제하기 버튼
        // 결제 패널 생성
        payPanel = new JPanel();
        payPanel.setLayout(new BorderLayout());
        payPanel.setBackground(new Color(0xFFFFFF));

        // 타이머가 표시된 라벨 생성
//        timerLabel = new JLabel("timer");
//        font = new Font("맑은 고딕", Font.BOLD, 40);
//        timerLabel.setFont(font);
//        timerLabel.setHorizontalAlignment(JLabel.CENTER); // 가운데 정렬



        // 결제 선택 패널 생성
        choicePanel = new JPanel();
        choicePanel.setBackground(new Color(0xFFFFFF));

        // 확인, 취소 버튼
        ok = new JButton("ok");
        cancel = new JButton("cancel");
        ok.setBackground(new Color(0x7EBCBE));
        cancel.setBackground(new Color(0x7EBCBE));
        font = new Font("맑은 고딕", Font.BOLD, 20);
        ok.setFont(font);
        cancel.setFont(font);


        // 확인, 취소 버튼에 리스너 붙이기
        ok.addActionListener(new OkBtnActionListener());
        cancel.addActionListener(new CancelBtnActionListener());


        // 결제 선택 패널에 타이머 라벨, 확인, 취소 버튼 붙이기
        JLabel str2 = new JLabel(" 결제를 진행하시겠습니까?");
        font = new Font("맑은 고딕", Font.BOLD, 20);
        str2.setFont(font);
        choicePanel.add(str2);
        choicePanel.add(ok);
        choicePanel.add(cancel);

        // 결제 선택 패널은 결제하기 버튼을 눌러야만 표시된다
//        timerLabel.setVisible(false);
        choicePanel.setVisible(false);

        // 결제하기 버튼
        payBtn = new JButton("결제하기");
        // 버튼에 액션 리스너 붙이기
        payBtn.addActionListener(new PayBtnActionListener());
        font = new Font("맑은 고딕", Font.BOLD, 30);
        payBtn.setFont(font);
        payBtn.setBackground(new Color(0xEED0CE));

        // 패널에 결제 화면, 결제버튼 붙이기
//        payPanel.add("North", timerLabel);
        payPanel.add("Center", choicePanel);
        payPanel.add("South", payBtn);

        // 패널에 장바구니 패널과 테이블 수정 버튼, 결제하기 버튼 붙이기
        add("North", basketPanel);
        add("Center", editBtnsPanel);
        add("South", payPanel);
    }



    // 테이블 클릭 마우스 리스너
    class TableMouseListener extends MouseAdapter {
        // 마우스 클릭 이벤트 발생
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == 1)  // 좌 클릭
                row = table.getSelectedRow(); // 선택되어진 row구하기
        }
    }


    // 장바구니의 reduce, remove, reset 버튼 리스너
    class EditBtnsActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            String b = btn.getText();

            int tableSize = model.getRowCount();


            // reset 버튼: 장바구니 초기화
            if(b.equals("초기화")){
                // 행이 모두 없어질 때까지 삭제
                while (model.getRowCount() != 0) {
                    model.removeRow(--tableSize);
                }
            }
            // reduce, remove 버튼
            else {
                String name;
                int quantity = 0;
                int price = 0;

                // 테이블에 특정 메뉴가 클릭된 상태일 때만 동작, 위에서 필드에 row=-1일떼로 초기화 시켜놓음
                if (row > -1) {
                    // 해당 메뉴 가격
                    name = (String) table.getValueAt(row, 0);

                    // reduce 버튼 : 해당 메뉴의 수량 하나씩 줄이기
                    if (b.equals("수량 감소")) {
                        // 해당 메뉴 수량 감소하기
                        quantity = Integer.parseInt((String) table.getValueAt(row, 1));
                        System.out.println(quantity);
                        quantity -= 1;
                        System.out.println(quantity);

                        // 수량이 0이 되면 행 삭제
                        if (quantity == 0) {
                            model.removeRow(row);
                            tableSize -= 1;
                            row = -1;
                        } else {
                            // 줄어진 수량의 가격 계산/ TabPanel의 메뉴를 찾아서 가격을 계산하는 방식
                            for(int i = 0; i < TabPanel.menuTitle.length-1; i++) {

                                for (int j = 0; j < TabPanel.menuTitle[i].length; j++) {
                                    if (name.equals(TabPanel.menuTitle[i][j])) {
                                        price = Integer.parseInt(TabPanel.menuPrice[i][j]);
                                    }
                                }
                            }
                            price = quantity * price; // 수량 * 가격

                            // 테이블 업데이트
                            table.setValueAt(Integer.toString(quantity), row, 1); // 수량
                            table.setValueAt(Integer.toString(price), row, 2); // 가격

                        }

                    }
                    // remove 버튼 : 해당 메뉴 삭제하기
                    else if (b.equals("품목 삭제")) {
                        model.removeRow(row);
                        tableSize -= 1;
                        row = -1;
                    }
                }
            } // else 끝

            // 합계 업데이트
            int sum = 0;
            for (int i = 0; i < tableSize; i++)
                sum += Integer.parseInt((String) model.getValueAt(i, 2));

            sumPrice.setText(Integer.toString(sum) + "원");

        }
    }

    // 결제 버튼 리스너
    class PayBtnActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            // 테이블이 비었다면
            if(model.getRowCount() == 0){
                // 장바구니 비었음을 알리는 다이얼로그 띄우기
                // 다이얼로그의 확인 버튼 색상, 버튼 폰트
                UIManager.put("Button.background", new Color(0xEED0CE));
                UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("맑은 고딕", Font.PLAIN, 15)));

                // 다이얼로그 라벨, 라벨 폰트
                JLabel jlabel = new JLabel("장바구니가 비었습니다.");
                jlabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));

                // 다이얼로그 띄우기
                JOptionPane.showMessageDialog(f, jlabel);
            }
            else{
                // 타이머가 스레드 생성
//                runnable = new TimerRunnable(timerLabel);
//                timerTh = new Thread();
//
//                // 결제창에 스레드 시작하기
//                timerTh.start();

                // 결제 화면 보이게 하기
//                timerLabel.setVisible(true);
                choicePanel.setVisible(true);
            }
        }
    }


    // ok 버튼(결제 확인 버튼) 리스너
    class OkBtnActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            // 결제 화면 숨기기
//            timerLabel.setVisible(false);
            choicePanel.setVisible(false);

            // 결제 화면에 스레드 종료 - 카운트 초기화
//            timerTh.interrupt();

            // 결제 완료 다이얼로그 띄우기
            // 다이얼로그의 확인 버튼 색상, 버튼 폰트
            UIManager.put("Button.background", new Color(0xEED0CE));
            UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("맑은 고딕", Font.PLAIN, 15)));

//            // 다이얼로그 라벨, 라벨 폰트
//            String str = "<html>"+sumPrice.getText() + " 결제 완료되었습니다.<br>감사합니다.</html>";

            String str = "결제내역은 아래와 같습니다." + "\n";
            int totalPrice = 0;
            try {
                Class.forName(JDBC_DRIVER);
                Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                for (int i = 0; i < model.getRowCount(); i++) {
                    String order = (String) model.getValueAt(i, 0);
                    int quantity = Integer.parseInt((String) model.getValueAt(i, 1));
                    int price = Integer.parseInt((String) model.getValueAt(i, 2));
                    totalPrice+=price;

                    str+= order + " " + quantity + "개 " + price + "원" + "\n";

                    // Your SQL query to insert data into the database
                    String sql = "INSERT INTO orderHistory (`order`, price, quantity) VALUES (?, ?, ?)";

                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        // Set the parameters for the prepared statement
                        statement.setString(1, order);
                        statement.setInt(2, quantity);
                        statement.setInt(3, price);

                        // Execute the insert query
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error inserting data into the database.");
                    }
                }
            }catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error connecting to the database.");
            }
            str+="총 결제금액은 " + totalPrice + "원 입니다.";

            JTextArea textArea = new JTextArea(str);
            textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 20));

            // 다이얼로그 띄우기
            JOptionPane.showMessageDialog(f, textArea);


            // 테이블 초기화
            int tableSize = model.getRowCount();
            while (model.getRowCount() != 0) {
                model.removeRow(--tableSize);
            }

            // 합계 초기화
            int sum = 0;
            sumPrice.setText(Integer.toString(sum) + "원");
        }
    }

    // cancel 버튼(결체 취소 버튼) 리스너
    class CancelBtnActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // 결제 화면 숨기기
//            timerLabel.setVisible(false);
            choicePanel.setVisible(false);
            // 결제 화면에 스레드 종료 - 카운트 초기화
//            timerTh.interrupt();

        }
    }



    // 타이머 스레드
//    class TimerRunnable implements Runnable{
//        private JLabel timerLabel; // 타이머 값이 출력된 레이블
//        public TimerRunnable(JLabel timerLabel){
//            this.timerLabel = timerLabel;
//        }
//
//        @Override
//        public void run(){
//            int count = 5; // 타이머 카운트 값
//            while(count >= 0){
//                timerLabel.setText(Integer.toString(count--));
//                try{
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                     e.printStackTrace();
//
//                    // 예외 발생시(ok 또는 cencel 버튼 눌렸을 때) 스레드 종료
//                    return;
//                }
//
//            }
//
//            if(count == -1){
//                // 시간 초과 다이얼로그 띄우기
//                // 다이얼로그 라벨, 라벨 폰트
//                JLabel jlabel = new JLabel("시간 초과로 결제가 취소됩니다.");
//                jlabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
//
//                // 다이얼로그 띄우기
//                JOptionPane.showMessageDialog(f, jlabel);
//
//                // 결제 화면 숨기기
//                timerLabel.setVisible(false);
//                choicePanel.setVisible(false);
//
//                // 테이블 초기화
//                int tableSize = model.getRowCount();
//                while (model.getRowCount() != 0) {
//                    model.removeRow(--tableSize);
//                }
//
//                // 합계 초기화
//                int sum = 0;
//                sumPrice.setText(Integer.toString(sum) + "원");
//
//                // 스레드 종료
//                return;
//            }
//        }
//    }
}
