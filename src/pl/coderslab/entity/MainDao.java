package pl.coderslab.entity;


public class MainDao {

    public static void main(String[] args) {

        UserDao userDao = new UserDao();
        User user = new User();

        User read = userDao.read(1);

        User userUpdate = userDao.read(2);
        userUpdate.setUserName("Marcin Marcin");
        userUpdate.setEmail("Marcin.Marcin@gmail.com");
        userUpdate.setPassword("MarcinPassword");
        userDao.update(userUpdate);

        User[] allUsers = userDao.findAll();
        for (User u : allUsers) {
            System.out.println(u);
        }

    }
}
