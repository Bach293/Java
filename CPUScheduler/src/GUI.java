
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.GraphicsEnvironment;

public class GUI
{
    private JFrame frame;
    private JPanel mainPanel;
    private CustomPanel chartPanel;
    private JScrollPane tablePane;
    private JScrollPane chartPane;
    private JTable table;
    private JButton addBtn;
    private JButton removeBtn;
    private JButton computeBtn;
    private JLabel wtLabel;
    private JLabel wtResultLabel;
    private JLabel tatLabel;
    private JLabel tatResultLabel;
    private JComboBox option;
    private DefaultTableModel model;
    
    public GUI()
    {
        model = new DefaultTableModel(new String[]{"Process", "AT", "BT", "WT", "TAT"}, 0);
        
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        tablePane = new JScrollPane(table);
        tablePane.setBounds(25, 25, 450, 250);
        
        addBtn = new JButton("Add");
        addBtn.setBounds(300, 280, 85, 25);
        addBtn.setBackground(Color.decode("#83c88a"));
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        addBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addRow(new String[]{"", "", "", "", ""});
            } 
        });
        
        removeBtn = new JButton("Remove");
        removeBtn.setBounds(390, 280, 85, 25);
        removeBtn.setBackground(Color.decode("#c58080"));
        removeBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        removeBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                
                if (row > -1) {
                    model.removeRow(row);
                }
            }
        });
        
        chartPanel = new CustomPanel();
        chartPanel.setPreferredSize(new Dimension(700, 10));
        chartPanel.setBackground(Color.WHITE);
        chartPane = new JScrollPane(chartPanel);
        chartPane.setBounds(25, 310, 450, 100);
        
        wtLabel = new JLabel("Average Waiting Time:");
        wtLabel.setBounds(25, 425, 180, 25);
        tatLabel = new JLabel("Average Turn Around Time:");
        tatLabel.setBounds(25, 450, 180, 25);
        wtResultLabel = new JLabel();
        wtResultLabel.setBounds(215, 425, 180, 25);
        tatResultLabel = new JLabel();
        tatResultLabel.setBounds(215, 450, 180, 25);
        
        option = new JComboBox(new String[]{"FCFS", "SJF", "SRT", "RR"});
        option.setBounds(390, 420, 85, 20);
        
        computeBtn = new JButton("Compute");
        computeBtn.setBounds(390, 450, 85, 25);
        computeBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        computeBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) option.getSelectedItem();
                CPUScheduler scheduler;
                
                switch (selected) {
                    case "FCFS":
                        scheduler = new FirstComeFirstServe();
                        break;
                    case "SJF":
                        scheduler = new ShortestJobFirst();
                        break;
                    case "SRT":
                        scheduler = new ShortestRemainingTime();
                        break;

                    case "RR":
                        String tq = JOptionPane.showInputDialog("Time Quantum");
                        if (tq == null) {
                            return;
                        }
                        scheduler = new RoundRobin();
                        scheduler.setTimeQuantum(Integer.parseInt(tq)); 
                        break;
                    default:
                        return;
                }
                
                for (int i = 0; i < model.getRowCount(); i++)
                {
                    String process = (String) model.getValueAt(i, 0);
                    int at = Integer.parseInt((String) model.getValueAt(i, 1));
                    int bt = Integer.parseInt((String) model.getValueAt(i, 2));
                             
                    scheduler.add(new Row(process, at, bt));
                }
                
                scheduler.process();
                
                for (int i = 0; i < model.getRowCount(); i++)
                {
                    String process = (String) model.getValueAt(i, 0);
                    Row row = scheduler.getRow(process);
                    model.setValueAt(row.getWaitingTime(), i, 3);
                    model.setValueAt(row.getTurnaroundTime(), i, 4);
                }
                
                wtResultLabel.setText(Double.toString(scheduler.getAverageWaitingTime()));
                tatResultLabel.setText(Double.toString(scheduler.getAverageTurnAroundTime()));
                
                chartPanel.setTimeline(scheduler.getTimeline());
            }
        });
        
        mainPanel = new JPanel(null);
        mainPanel.setPreferredSize(new Dimension(500, 500));
        mainPanel.setBackground(Color.decode("#7096b8"));
        mainPanel.add(tablePane);
        mainPanel.add(addBtn);
        mainPanel.add(removeBtn);
        mainPanel.add(chartPane);
        mainPanel.add(wtLabel);
        mainPanel.add(tatLabel);
        mainPanel.add(wtResultLabel);
        mainPanel.add(tatResultLabel);
        mainPanel.add(option);
        mainPanel.add(computeBtn);
        
        frame = new JFrame("CPU Scheduler Simulator");
        // Lấy kích thước màn hình
        Dimension screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();

        // Tính toán kích thước khung dựa trên kích thước màn hình
        int frameWidth = (int) (screenSize.width * 0.30);
        int frameHeight = (int) (screenSize.height * 0.60);

        // Đặt kích thước cho khung
        frame.setSize(frameWidth, frameHeight);

        // Đặt vị trí của khung ở giữa màn hình
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(mainPanel);
        frame.pack();
    }
    
    public static void main(String[] args)
    {
        new GUI();
    }
    
    class CustomPanel extends JPanel
    {   
        private List<Event> timeline;
        
        @Override
        protected void paintComponent(Graphics g)
{
    super.paintComponent(g);
    
    if (timeline != null)
    {
        for (int i = 0; i < timeline.size(); i++)
        {
            Event event = timeline.get(i);
            int x = 30 * (i + 1);
            int y = 20;
            
            // Lấy màu cho block dựa trên tên quy trình
            Color color = getColorForProcess(event.getProcessName());
            
            // Vẽ block với màu tương ứng
            g.setColor(color);
            g.fillRect(x, y, 30, 30);
            
            // Thiết lập font chữ và vẽ tên quy trình màu đen giữa block
            g.setColor(Color.BLACK);
            g.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g.drawString(event.getProcessName(), x + 10, y + 20);
            
            // Thiết lập font chữ và vẽ thời gian bắt đầu màu đen
            g.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g.drawString(Integer.toString(event.getStartTime()), x - 5, y + 45);
            
            if (i == timeline.size() - 1)
            {
                // Vẽ thời gian kết thúc màu đen nếu là sự kiện cuối cùng
                g.drawString(Integer.toString(event.getFinishTime()), x + 27, y + 45);
            }
        }
    }
}

// Phương thức hỗ trợ để lấy màu cho quy trình
private Color getColorForProcess(String processName)
{
    // Tạo một danh sách các màu để gán cho các quy trình
    Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.MAGENTA};
    
    // Sử dụng hàm hash code của tên quy trình để lấy vị trí màu trong danh sách
    int colorIndex = Math.abs(processName.hashCode()) % colors.length;
    
    // Trả về màu tương ứng
    return colors[colorIndex];
}
        
        public void setTimeline(List<Event> timeline)
        {
            this.timeline = timeline;
            repaint();
        }
    }
}
