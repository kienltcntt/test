import java.util.*;

// Enums
enum RoomType {
    KING,
    DOUBLE,
    JUNIOR_SUITE,
    SUITE
}

abstract class Room {
    private int roomNumber;
    private RoomType type;
    private boolean availability;

    public Room(int roomNumber, RoomType type) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.availability = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return type;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public abstract double getPricePerNight();

    @Override
    public String toString() {
        return "Room Number: " + roomNumber + ", Type: " + type + ", Availability: " + (availability ? "Available" : "Occupied");
    }
}

class KingRoom extends Room {
    private static final double PRICE_PER_NIGHT = 200;

    public KingRoom(int roomNumber) {
        super(roomNumber, RoomType.KING);
    }

    @Override
    public double getPricePerNight() {
        return PRICE_PER_NIGHT;
    }
}

class DoubleRoom extends Room {
    private static final double PRICE_PER_NIGHT = 250;

    public DoubleRoom(int roomNumber) {
        super(roomNumber, RoomType.DOUBLE);
    }

    @Override
    public double getPricePerNight() {
        return PRICE_PER_NIGHT;
    }
}

class JuniorSuite extends Room {
    private static final double PRICE_PER_NIGHT = 300;

    public JuniorSuite(int roomNumber) {
        super(roomNumber, RoomType.JUNIOR_SUITE);
    }

    @Override
    public double getPricePerNight() {
        return PRICE_PER_NIGHT;
    }
}

class Suite extends Room {
    private static final double PRICE_PER_NIGHT = 400;

    public Suite(int roomNumber) {
        super(roomNumber, RoomType.SUITE);
    }

    @Override
    public double getPricePerNight() {
        return PRICE_PER_NIGHT;
    }
}

class In {
    private static Scanner in = new Scanner(System.in);

    public static String nextLine() {
        return in.nextLine();
    }

    public static char nextChar() {
        return in.nextLine().charAt(0);
    }

    public static char nextUpperChar() {
        return in.nextLine().toUpperCase().charAt(0);
    }

    public static int nextInt() {
        int i = in.nextInt();
        in.nextLine();
        return i;
    }
}

class Guest {
    private int guestID;
    private String name;
    private String phoneNumber;

    public Guest(int guestID, String name, String phoneNumber) {
        this.guestID = guestID;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public int getGuestID() {
        return guestID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Guest ID: " + guestID + ", Name: " + name + ", Phone: " + phoneNumber;
    }
}

class Reservation {
    private Guest guest;
    private Room room;

    public Reservation(Guest guest, Room room) {
        this.guest = guest;
        this.room = room;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Guest: " + guest.getName() + ", Phone: " + guest.getPhoneNumber() + ", Room: " + room.getRoomNumber();
    }
}

interface ReservationManager {
    void addReservation(Reservation reservation);

    void cancelReservation(String guestName, String guestPhoneNumber);

    void updateReservation(String guestName, String guestPhoneNumber);

    void showAllReservations();

    Reservation findReservation(String guestName, String guestPhoneNumber);
}


class HotelReservationManager implements ReservationManager {
    protected List<Reservation> reservations;
    protected List<Room> availableRooms;

    public HotelReservationManager() {
        this.reservations = new ArrayList<>();
        this.availableRooms = initializeAvailableRooms();
    }

    protected List<Room> initializeAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Room room;
            if (i <= 4) {
                room = new KingRoom(i);
            } else if (i <= 8) {
                room = new DoubleRoom(i);
            } else if (i == 9) {
                room = new JuniorSuite(i);
            } else {
                room = new Suite(i);
            }
            rooms.add(room);
        }
        return rooms;
    }

    @Override
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        System.out.println("Reservation added successfully.");
        Room reservedRoom = reservation.getRoom();
        for (Room room : availableRooms) {
            if (room.getRoomNumber() == reservedRoom.getRoomNumber()) {
                room.setAvailability(false);
                break;
            }
        }
    }


    @Override
    public void cancelReservation(String guestName, String guestPhoneNumber) {
        Reservation reservation = findReservation(guestName, guestPhoneNumber);
        if (reservation != null) {
            reservations.remove(reservation);
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println("Reservation not found.");
        }
    }

    @Override
    public void updateReservation(String guestName, String guestPhoneNumber) {
        Reservation reservation = findReservation(guestName, guestPhoneNumber);
        if (reservation != null) {
            System.out.println("Enter new guest name:");
            String newGuestName = In.nextLine();
            System.out.println("Enter new guest phone number:");
            String newGuestPhoneNumber = In.nextLine();
            reservation.getGuest().setName(newGuestName);
            reservation.getGuest().setPhoneNumber(newGuestPhoneNumber);

            System.out.println("Enter new room number:");
            int newRoomNumber = In.nextInt();
            if (newRoomNumber >= 1 && newRoomNumber <= 4) {
                reservation.setRoom(new KingRoom(newRoomNumber));
            } else if (newRoomNumber >= 5 && newRoomNumber <= 8) {
                reservation.setRoom(new DoubleRoom(newRoomNumber));
            } else if (newRoomNumber == 9) {
                reservation.setRoom(new JuniorSuite(newRoomNumber));
            } else if (newRoomNumber == 10) {
                reservation.setRoom(new Suite(newRoomNumber));
            } else {
                System.out.println("Invalid room number.");
                return;
            }

            System.out.println("Reservation updated successfully.");
        } else {
            System.out.println("Reservation not found.");
        }
    }

    @Override
    public void showAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            System.out.println("All Reservations:");
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    @Override
    public Reservation findReservation(String guestName, String guestPhoneNumber) {
        for (Reservation reservation : reservations) {
            Guest guest = reservation.getGuest();
            if (guest.getName().equals(guestName) && guest.getPhoneNumber().equals(guestPhoneNumber)) {
                return reservation;
            }
        }
        return null;
    }
}

public class ProjectA {
    public static void main(String[] args) {
        printWelcomeMessage();
        ReservationManager reservationManager = new HotelReservationManager();

        boolean running = true;
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add Reservation");
            System.out.println("2. Cancel Reservation");
            System.out.println("3. Update Reservation");
            System.out.println("4. Show All Reservations");
            System.out.println("5. Display Available Rooms");
            System.out.println("6. Exit");

            int choice = In.nextInt();
            if (choice == 1) {
                addReservation(reservationManager);
            } else if (choice == 2) {
                cancelReservation(reservationManager);
            } else if (choice == 3) {
                updateReservation(reservationManager);
            } else if (choice == 4) {
                reservationManager.showAllReservations();
            } else if (choice == 5) {
                displayAvailableRooms(((HotelReservationManager) reservationManager).availableRooms);
            } else if (choice == 6) {
                running = false;
                System.out.println("Exiting program...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printWelcomeMessage() {
        System.out.println("Welcome to Marriott Hotel Reservation System!");
        System.out.println("--------------------------------------------------");
    }

    private static void updateReservation(ReservationManager reservationManager) {
        System.out.println("Enter guest name:");
        String guestName = In.nextLine();
        System.out.println("Enter guest phone number:");
        String guestPhoneNumber = In.nextLine();
        reservationManager.updateReservation(guestName, guestPhoneNumber);
    }

    public static void addReservation(ReservationManager reservationManager) {
        System.out.println("Enter guest name:");
        String guestName = In.nextLine();
        System.out.println("Enter guest phone number:");
        String guestPhoneNumber = In.nextLine();
        Guest guest = new Guest(1, guestName, guestPhoneNumber);

        System.out.println("Enter room number:");
        int roomNumber = In.nextInt();

        Room room;
        if (roomNumber >= 1 && roomNumber <= 4) {
            room = new KingRoom(roomNumber);
        } else if (roomNumber >= 5 && roomNumber <= 8) {
            room = new DoubleRoom(roomNumber);
        } else if (roomNumber == 9) {
            room = new JuniorSuite(roomNumber);
        } else if (roomNumber == 10) {
            room = new Suite(roomNumber);
        } else {
            System.out.println("Invalid room number.");
            return;
        }

        Reservation reservation = new Reservation(guest, room);
        reservationManager.addReservation(reservation);
    }

    private static void cancelReservation(ReservationManager reservationManager) {
        System.out.println("Enter guest name:");
        String guestName = In.nextLine();
        System.out.println("Enter guest phone number:");
        String guestPhoneNumber = In.nextLine();
        reservationManager.cancelReservation(guestName, guestPhoneNumber);
    }

    private static void displayAvailableRooms(List<Room> availableRooms) {
        System.out.println("Available Rooms:");

        for (Room room : availableRooms) {
            if (room.isAvailable()) {
                System.out.println(room);
            }
        }
    }
}
