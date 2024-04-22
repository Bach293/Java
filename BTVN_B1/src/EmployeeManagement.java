import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class EmployeeManagement {
	private static Scanner input = new Scanner(System.in);
	
    public static int menu() {
        System.out.print("Menu:"
                + "\n [1] Hiển thị danh sách nhân viên"
                + "\n [2] Thêm nhân viên"
                + "\n [3] Xóa nhân viên"
                + "\n [4] Thoát"
                + "\n Hãy lựa chọn: ");
        int option;
        try {
            option = input.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            input.nextLine();
            option = menu();
        }
        input.nextLine();
        return option;
    }

    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Employee employee = new Employee("NV0" + i, "Name", 22, "P0" + i, "C0" + i, 5.5);
            employees.add(employee);
        }

        boolean exit = false;
        while (!exit) {
            int option = menu();
            switch (option) {
                case 1:
                    System.out.println("Danh sách nhân viên:");
                    for (Employee employee : employees) {
                        System.out.println("ID: " + employee.getId()
                                + "  Name: " + employee.getName()
                                + "  Age: " + employee.getAge()
                                + "  Department: " + employee.getDepartment()
                                + "  Code: " + employee.getCode()
                                + "  Salary rate: " + employee.getSalaryRate());
                    }
                    break;
                case 2:
                    System.out.println("Thêm nhân viên:");
                    System.out.print("Nhập ID: ");
                    String id = input.nextLine();
                    System.out.print("Nhập tên: ");
                    String name = input.nextLine();
                    System.out.print("Nhập tuổi: ");
                    int age = input.nextInt();
                    input.nextLine(); 
                    System.out.print("Nhập phòng ban: ");
                    String department = input.nextLine();
                    System.out.print("Nhập mã: ");
                    String code = input.nextLine();
                    System.out.print("Nhập tỷ lệ lương: ");
                    double salaryRate = input.nextDouble();
                    input.nextLine(); 

                    Employee newEmployee = new Employee(id, name, age, department, code, salaryRate);
                    employees.add(newEmployee);
                    System.out.println("Đã thêm nhân viên mới thành công.");
                    
                    break;
                case 3:
                    System.out.println("Xóa nhân viên:");

                    int lastIndex = employees.size() - 1;
                    if (lastIndex >= 0) {
                        Employee employeeToRemove = employees.get(lastIndex);
                        employees.remove(employeeToRemove);
                        System.out.println("Đã xóa nhân viên cuối cùng.");
                    } else {
                        System.out.println("Danh sách nhân viên rỗng. Không có nhân viên để xóa.");
                    }
                    break;
                case 4:
                    exit = true;
                    System.out.println("Chương trình kết thúc.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }
        
        input.close();
    }
}