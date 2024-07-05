import java.io.*;
import java.util.Scanner;

class Book implements Serializable {
    private String bno;
    private String bname;
    private String aname;

    public void createBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nNEW BOOK ENTRY...\n");
        System.out.print("\nEnter The book no.: ");
        bno = scanner.nextLine();
        System.out.print("\nEnter The Name of The Book: ");
        bname = scanner.nextLine();
        System.out.print("\nEnter The Author's Name: ");
        aname = scanner.nextLine();
        System.out.println("\n\nBook Created..");
    }

    public void showBook() {
        System.out.println("\nBook no. : " + bno);
        System.out.println("Book Name : " + bname);
        System.out.println("Author Name : " + aname);
    }

    public void modifyBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nBook no. : " + bno);
        System.out.print("\nModify Book Name : ");
        bname = scanner.nextLine();
        System.out.print("\nModify Author's Name of Book : ");
        aname = scanner.nextLine();
    }

    public String retbno() {
        return bno;
    }

    public void report() {
        System.out.printf("%-6s %-30s %-20s\n", bno, bname, aname);
    }
}

class Student implements Serializable {
    private String admno;
    private String name;
    private String stbno;
    private int token;

    public void createStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nNEW STUDENT ENTRY...\n");
        System.out.print("\nEnter The admission no.: ");
        admno = scanner.nextLine();
        System.out.print("\nEnter The Name of The Student: ");
        name = scanner.nextLine();
        token = 0;
        stbno = "";
        System.out.println("\n\nStudent Record Created..");
    }

    public void showStudent() {
        System.out.println("\nAdmission no. : " + admno);
        System.out.println("Student Name : " + name);
        System.out.println("No of Book issued : " + token);
        if (token == 1) {
            System.out.println("Book No " + stbno);
        }
    }

    public void modifyStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nAdmission no. : " + admno);
        System.out.print("\nModify Student Name : ");
        name = scanner.nextLine();
    }

    public String retadmno() {
        return admno;
    }

    public String retstbno() {
        return stbno;
    }

    public int rettoken() {
        return token;
    }

    public void addtoken() {
        token = 1;
    }

    public void resettoken() {
        token = 0;
    }

    public void getstbno(String t) {
        stbno = t;
    }

    public void report() {
        System.out.printf("%-6s %-20s %-10d\n", admno, name, token);
    }
}

public class LibraryManagementSystem {
    private static File bookFile = new File("book.dat");
    private static File studentFile = new File("student.dat");
    private static Book bk = new Book();
    private static Student st = new Student();

    private static void writeBook() {
        char ch;
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(bookFile, true))) {
            do {
                bk.createBook();
                out.writeObject(bk);
                System.out.print("\n\nDo you want to add more record..(y/n?) ");
                ch = (char) System.in.read();
                System.in.read(); // consume newline
            } while (ch == 'y' || ch == 'Y');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeStudent() {
        char ch;
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(studentFile, true))) {
            do {
                st.createStudent();
                out.writeObject(st);
                System.out.print("\n\ndo you want to add more record..(y/n?) ");
                ch = (char) System.in.read();
                System.in.read(); // consume newline
            } while (ch == 'y' || ch == 'Y');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displaySpb(String n) {
        System.out.println("\nBOOK DETAILS\n");
        boolean flag = false;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(bookFile))) {
            while (true) {
                bk = (Book) in.readObject();
                if (bk.retbno().equals(n)) {
                    bk.showBook();
                    flag = true;
                }
            }
        } catch (EOFException e) {
            if (!flag)
                System.out.println("\n\nBook does not exist");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void displaySps(String n) {
        System.out.println("\nSTUDENT DETAILS\n");
        boolean flag = false;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(studentFile))) {
            while (true) {
                st = (Student) in.readObject();
                if (st.retadmno().equals(n)) {
                    st.showStudent();
                    flag = true;
                }
            }
        } catch (EOFException e) {
            if (!flag)
                System.out.println("\n\nStudent does not exist");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void modifyBook() {
        String n;
        boolean found = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n\tMODIFY BOOK RECORD.... ");
        System.out.print("\n\n\tEnter The book no. of The book: ");
        n = scanner.nextLine();
        try (RandomAccessFile raf = new RandomAccessFile(bookFile, "rw")) {
            long pos;
            while ((pos = raf.getFilePointer()) < raf.length()) {
                bk = (Book) new ObjectInputStream(new ByteArrayInputStream(raf.readNBytes((int) (raf.length() - pos)))).readObject();
                if (bk.retbno().equals(n)) {
                    bk.showBook();
                    System.out.println("\nEnter The New Details of book");
                    bk.modifyBook();
                    raf.seek(pos);
                    new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(bk);
                    System.out.println("\n\n\t Record Updated");
                    found = true;
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!found)
            System.out.println("\n\n Record Not Found ");
    }

    private static void modifyStudent() {
        String n;
        boolean found = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n\tMODIFY STUDENT RECORD... ");
        System.out.print("\n\n\tEnter The admission no. of The student: ");
        n = scanner.nextLine();
        try (RandomAccessFile raf = new RandomAccessFile(studentFile, "rw")) {
            long pos;
            while ((pos = raf.getFilePointer()) < raf.length()) {
                st = (Student) new ObjectInputStream(new ByteArrayInputStream(raf.readNBytes((int) (raf.length() - pos)))).readObject();
                if (st.retadmno().equals(n)) {
                    st.showStudent();
                    System.out.println("\nEnter The New Details of student");
                    st.modifyStudent();
                    raf.seek(pos);
                    new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(st);
                    System.out.println("\n\n\t Record Updated");
                    found = true;
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!found)
            System.out.println("\n\n Record Not Found ");
    }

    private static void deleteStudent() {
        String n;
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n\n\tDELETE STUDENT...");
        System.out.print("\n\nPlease Enter The admission no. You Want To Delete: ");
        n = scanner.nextLine();
        boolean flag = false;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(studentFile));
             ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Temp.dat"))) {
            while (true) {
                st = (Student) in.readObject();
                if (!st.retadmno().equals(n)) {
                    out.writeObject(st);
                } else {
                    flag = true;
                }
            }
        } catch (EOFException e) {
            if (flag) {
                studentFile.delete();
                new File("Temp.dat").renameTo(studentFile);
                System.out.println("\n\n\tRecord Deleted ..");
            } else {
                System.out.println("\n\nRecord not found");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void deleteBook() {
        String n;
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n\n\tDELETE BOOK...");
        System.out.print("\n\nPlease Enter The book no. You Want To Delete: ");
        n = scanner.nextLine();
        boolean flag = false;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(bookFile));
             ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Temp.dat"))) {
            while (true) {
                bk = (Book) in.readObject();
                if (!bk.retbno().equals(n)) {
                    out.writeObject(bk);
                } else {
                    flag = true;
                }
            }
        } catch (EOFException e) {
            if (flag) {
                bookFile.delete();
                new File("Temp.dat").renameTo(bookFile);
                System.out.println("\n\n\tRecord Deleted ..");
            } else {
                System.out.println("\n\nRecord not found");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void displayAllBooks() {
        System.out.println("\n\n\t\tBook LIST\n\n");
        System.out.println("=====================================================");
        System.out.println("Book Number      Book Name              Author");
        System.out.println("=====================================================");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(bookFile))) {
            while (true) {
                bk = (Book) in.readObject();
                bk.report();
            }
        } catch (EOFException e) {
            // End of File reached
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void displayAllStudents() {
        System.out.println("\n\n\t\tSTUDENT LIST\n\n");
        System.out.println("===================================================================");
        System.out.println("Admission Number      Name               Book Issued");
        System.out.println("===================================================================");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(studentFile))) {
            while (true) {
                st = (Student) in.readObject();
                st.report();
            }
        } catch (EOFException e) {
            // End of File reached
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void bookIssue() {
        String sn, bn;
        boolean found = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\nBOOK ISSUE ...");
        System.out.print("\n\n\tEnter The student's admission no.: ");
        sn = scanner.nextLine();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(studentFile));
             ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Temp.dat"))) {
            while (true) {
                st = (Student) in.readObject();
                if (st.retadmno().equals(sn)) {
                    if (st.rettoken() == 0) {
                        displayAllBooks();
                        System.out.print("\n\n\tEnter the book no.: ");
                        bn = scanner.nextLine();
                        try (ObjectInputStream inBook = new ObjectInputStream(new FileInputStream(bookFile));
                             ObjectOutputStream outBook = new ObjectOutputStream(new FileOutputStream("TempBook.dat"))) {
                            while (true) {
                                bk = (Book) inBook.readObject();
                                if (bk.retbno().equals(bn)) {
                                    bk.showBook();
                                    st.addtoken();
                                    st.getstbno(bn);
                                    outBook.writeObject(bk);
                                    System.out.println("\n\n\t Book issued successfully\n\nPlease Note: Write current date in backside of your book and submit within 15 days. Fine Rs. 1 for each day after 15 days period");
                                    found = true;
                                } else {
                                    outBook.writeObject(bk);
                                }
                            }
                        } catch (EOFException e) {
                            if (!found)
                                System.out.println("Book does not exist");
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("You have not returned the last book ");
                    }
                    out.writeObject(st);
                } else {
                    out.writeObject(st);
                }
            }
        } catch (EOFException e) {
            if (!found)
                System.out.println("Student record not exist...");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        studentFile.delete();
        new File("Temp.dat").renameTo(studentFile);
        bookFile.delete();
        new File("TempBook.dat").renameTo(bookFile);
    }

    private static void bookDeposit() {
        String sn, bn;
        boolean found = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\nBOOK DEPOSIT ...");
        System.out.print("\n\n\tEnter The student's admission no.: ");
        sn = scanner.nextLine();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(studentFile));
             ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Temp.dat"))) {
            while (true) {
                st = (Student) in.readObject();
                if (st.retadmno().equals(sn)) {
                    if (st.rettoken() == 1) {
                        displayAllBooks();
                        System.out.print("\n\n\tEnter the book no.: ");
                        bn = scanner.nextLine();
                        try (ObjectInputStream inBook = new ObjectInputStream(new FileInputStream(bookFile));
                             ObjectOutputStream outBook = new ObjectOutputStream(new FileOutputStream("TempBook.dat"))) {
                            while (true) {
                                bk = (Book) inBook.readObject();
                                if (bk.retbno().equals(bn)) {
                                    bk.showBook();
                                    st.resettoken();
                                    st.getstbno("");
                                    outBook.writeObject(bk);
                                    System.out.println("\n\n\t Book deposited successfully");
                                    found = true;
                                } else {
                                    outBook.writeObject(bk);
                                }
                            }
                        } catch (EOFException e) {
                            if (!found)
                                System.out.println("Book does not exist");
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("No book issued to this student");
                    }
                    out.writeObject(st);
                } else {
                    out.writeObject(st);
                }
            }
        } catch (EOFException e) {
            if (!found)
                System.out.println("Student record not exist...");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        studentFile.delete();
        new File("Temp.dat").renameTo(studentFile);
        bookFile.delete();
        new File("TempBook.dat").renameTo(bookFile);
    }

    private static void start() {
        Scanner scanner = new Scanner(System.in);
        char ch;
        do {
            System.out.println("\n\n\n\tMAIN MENU");
            System.out.println("\n\tA. CREATE STUDENT RECORD");
            System.out.println("\n\tB. DISPLAY ALL STUDENTS RECORD");
            System.out.println("\n\tC. DISPLAY SPECIFIC STUDENT RECORD ");
            System.out.println("\n\tD. MODIFY STUDENT RECORD");
            System.out.println("\n\tE. DELETE STUDENT RECORD");
            System.out.println("\n\tF. CREATE BOOK ");
            System.out.println("\n\tG. DISPLAY ALL BOOKS ");
            System.out.println("\n\tH. DISPLAY SPECIFIC BOOK ");
            System.out.println("\n\tI. MODIFY BOOK ");
            System.out.println("\n\tJ. DELETE BOOK ");
            System.out.println("\n\tK. ISSUE BOOK ");
            System.out.println("\n\tL. DEPOSIT BOOK ");
            System.out.println("\n\tM. EXIT");
            System.out.println("\n\n\tPlease Select Your Option (1-13) ");
            ch = scanner.next().charAt(0);
            scanner.nextLine(); // Consume newline
            switch (ch) {
                case 'A':
                    writeStudent();
                    break;
                case 'B':
                    displayAllStudents();
                    break;
                case 'C':
                    String admno;
                    System.out.print("\n\n\tPlease Enter The Admission No. ");
                    admno = scanner.nextLine();
                    displaySps(admno);
                    break;
                case 'D':
                    modifyStudent();
                    break;
                case 'E':
                    deleteStudent();
                    break;
                case 'F':
                    writeBook();
                    break;
                case 'G':
                    displayAllBooks();
                    break;
                case 'H':
                    String bno;
                    System.out.print("\n\n\tPlease Enter The book No. ");
                    bno = scanner.nextLine();
                    displaySpb(bno);
                    break;
                case 'I':
                    modifyBook();
                    break;
                case 'J':
                    deleteBook();
                    break;
                case 'K':
                    bookIssue();
                    break;
                case 'L':
                    bookDeposit();
                    break;
                case 'M':
                    System.exit(0);
                default:
                    System.out.println("\nInvalid Option, Please Try Again.");
            }
        } while (ch != 'M');
    }

    public static void main(String[] args) {
        start();
    }
}

