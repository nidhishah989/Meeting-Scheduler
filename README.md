# Meetsy - Meeting Scheduler Platform

## Description
Meetsy is a Meeting Scheduler Platform that simplifies the process of scheduling meetings for organizations. It provides a seamless experience for both teams and clients, allowing efficient organization management and hassle-free meeting scheduling.

## Steps to Setup

1. **Clone this Repository:**
   ```bash
   git clone https://github.com/nidhishah989/Meeting-Scheduler/tree/availability
2. **Set up MySQL database:**

    Create a MySQL database named `meetsy`.

3. **Configure your database credentials:**

   Update the database configuration in the `application.properties` file located in `src/main/resources/`.

## Run the App
1. Run the app using IntelliJ IDE with Maven.
2. The app will start running at http://localhost:8080.

## User Roles:

- **Admin:** Admins can manage organizations, teams, and clients associated with that organization.
- **Team Member:** Team members have the ability to manage their meeting availability, meeting types, and scheduled meetings.
- **Client:** Clients can schedule meetings with team members by selecting an available meeting window.

## Organization Setup:

To set up your organization on this platform:

1. From the main page, click on the "Let's Setup" button.
2. Fill out the organization name and your credentials to set yourself as the admin of that organization on the Meetsy platform.
3. After successful signup, provide more details about your organization.
4. After successfully setting up the organization, you are ready to set your meeting availability.

## Admin Dashboard:

As an admin of the organization on the Meetsy platform, you can:

1. Manage your team and clients through the dashboard. 
2. Invite your team members by clicking "Add Team Member."
3. Invite your clients by clicking "Add Client."
4. Update your upcoming availability by clicking "Update Availability."

## Team Member or Client Sign-up:

1. From the main page, click on "Sign Up."
2. Provide your email address to receive a sign-up email from Meetsy.
3. Check your email for a temporary passcode and organization details.
4. Set your password and enjoy the meeting scheduler platform.

## Meeting Availability Setup:

Configure your meeting availability:

1. Choose your meeting type (OnSite or Zoom).
2. Set your timezone for accurate scheduling.
3. Define your general weekly availability for each day.
   - Note: The front availability is in 24-hour format (e.g., 1:00 pm is 13:00).
4. Customize default availability and add different timeslots.
   - Press the '+' button for each day to add available timeslots.
5. Meetsy's backend will distribute your availability into 30-minute meeting windows for clients.


Certainly! Here's the code for the "Schedule Meeting" section in Markdown format:

markdown
Copy code
## Schedule Meeting

For clients, scheduling a meeting on Meetsy is a breeze. Follow these simple steps after a successful login:

1. **Choose Team Member:**
   - After logging in, select the team member with whom you want to schedule a meeting.

2. **Choose Meeting Date:**
   - In the second form, pick the desired date for your meeting.
   - Upon selection, a second form will appear, showcasing the team member's available 30-minute time slots for that specific date.

3. **Select Meeting Type and Time Slot:**
   - Note: Time slots are displayed in the team member's timezone, which is also mentioned in the form.
   - Choose your preferred meeting type (OnSite or Zoom) and select a suitable time slot.

4. **Schedule the Meeting:**
   - Complete the scheduling process by confirming your meeting details.

# File Structure
      Meetsy
      |-- src
      |   |-- main
      |   |   |-- java
      |   |   |   `-- org/nidhishah/meetingscheduler/   # Java source code packages
      |   |   |-- resources
      |   |   |   |-- static                          # Static resources like CSS, JS, images
      |   |   |   `-- templates                       # Thymeleaf HTML templates
      |   `-- test                                    # Test source code
      |-- pom.xml                                     # Maven Project Object Model file
      |-- application.properties                      # Configuration file for application properties
      `-- README.md                                   # Project documentation and README file
# Testing

To ensure the proper functioning of Meetsy, follow these testing steps:

1. **Configure application.properties:**
   - Set the Hibernate DDL auto property to create tables:
     ```bash
     spring.jpa.hibernate.ddl-auto=create
     ```

2. **Repository Tests:**
   - Navigate to the testing files under `src/main/test/repository`.
   - Run the following tests:
      - Role Repository Test (Parameterized Test)
      - Organization Repository Test
      - User Repository Test
      - TeamMemberAvailability Repository Test

3. **Service Tests:**
   - Navigate to the testing files under `src/main/test/services`.
   - Run the following tests:
      - Role Service Test (Parameterized Test)
      - Organization Service Test
      - User Service Test
      - TeamMember Availability Service Test
      - Client Service Test

Role-related tests include parameterized testing for comprehensive coverage.

## Technologies Used

- Spring Boot ⚙️
- Bootstrap 🅱️
- Thymeleaf 🌐
- CSS 🎨
- HTML 📄
- Spring Security 🔒
- MySQL 🐬
- Bootstrap Timepicker ⏰



