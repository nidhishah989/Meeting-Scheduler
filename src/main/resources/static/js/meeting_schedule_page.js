const calendarBody = document.getElementById('calendarBody');
const currentDate = new Date();
let currentMonth = currentDate.getMonth();
let currentYear = currentDate.getFullYear();
let previousday ="";

function generateCalendar() {
    const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
    const firstDay = new Date(currentYear, currentMonth, 1).getDay();

    let calendarContent = '';

    let daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    for (let dayOfWeek of daysOfWeek) {
        calendarContent += `<div class="calendar-day-week">${dayOfWeek}</div>`;
    }

    for (let i = 0; i < firstDay; i++) {
        calendarContent += '<div class="calendar-day"></div>';
    }

    for (let day = 1; day <= daysInMonth; day++) {
        if (day === currentDate.getDate() && currentMonth === currentDate.getMonth() && currentYear === currentDate.getFullYear()) {
            calendarContent += `<div class="calendar-day current-day" onclick="setSelectedDate(${day})">${day}</div>`;
        } else {
            calendarContent += `<div class="calendar-day"  onclick="setSelectedDate(${day})">${day}</div>`;
        }
    }

    calendarBody.innerHTML = calendarContent;
}

function prevMonth() {
    if (currentMonth === 0) {
        currentYear -= 1;
        currentMonth = 11;
    } else {
        currentMonth -= 1;
    }
    updateMonthYear();
    generateCalendar();
}

function nextMonth() {
    if (currentMonth === 11) {
        currentYear += 1;
        currentMonth = 0;
    } else {
        currentMonth += 1;
    }
    updateMonthYear();
    generateCalendar();
}

function updateMonthYear() {
    const monthYear = new Date(currentYear, currentMonth);
    document.getElementById('monthYear').innerText = monthYear.toLocaleString('default', { month: 'long' }) + ' ' + currentYear;
}

function selectTimeSlot(day, timeSlot) {
    console.log(day)
    console.log(timeSlot)
    document.getElementById('selectedTimeSlot').value = timeSlot; // Set the selected time zone
    document.getElementById('selectedDay').value = day;
}

function setSelectedDate(day) {
    console.log(day); // Day number
    console.log(currentMonth); // Current month (0-11)
    console.log(currentYear); // Current year
    const selectedDay = new Date(currentYear, currentMonth, day);
    console.log(selectedDay.toString()); // Full date representation
    const dayInFullText = selectedDay.toLocaleDateString('en-US', { weekday: 'long' });
    const formattedDate = selectedDay.toLocaleDateString('en-US', { month: '2-digit', day: '2-digit', year: 'numeric' });

    console.log(formattedDate); // MM/DD/YYYY format
    console.log(dayInFullText); // Full day name (e.g., Monday, Tuesday)
    //display side form by removing d-none
    const form = document.getElementById("formColumn");
    form.classList.remove("d-none");
    // in side form- date change as per selection
    const selectedDate = document.getElementById("selectedDate");
    selectedDate.value = formattedDate;
    const calendercolumn = document.getElementById("calenderside");
    // Get the screen width
    const mediaQuery = window.matchMedia('(max-width: 767px)');
    if (mediaQuery.matches) {
        // Mobile size or smaller
        calendercolumn.style.display = "none"; // Hide the calenderside for mobile
    } else {
        document.getElementById("calenderside").classList.remove("offset-md-3");
    }
    // show the day timeslots as per selection
    const timeslotblock = document.getElementById(dayInFullText)
    console.log("previous day: "+ previousday)
    if (previousday != ""){
        console.log("changing the day..")
        const previoustimeSlotblock = document.getElementById(previousday)
        previoustimeSlotblock.classList.add("d-none")
    }
    timeslotblock.classList.remove("d-none")
    previousday=dayInFullText;


}

function checkForm(event) {
    const meetingType = document.getElementById("meetingType").value;
    const selectedTimeSlot = document.getElementById("selectedTimeSlot").value;
    const selectedDate = document.getElementById("selectedDate").value;
    console.log("meetingType: ",meetingType);
    console.log("selectedTimeSlot: ",selectedTimeSlot)
    console.log("selectedDate: ",selectedDate)
    if (meetingType !== "OnSite" && meetingType !== "Zoom") {
        alert("Please select a Meeting Type.");
        return false;
    } else if (selectedTimeSlot === "timeSlot" && selectedDay ==="day") {
        alert("Please select a meeting Window to schedule meeting.");
        return false;
    } else if (selectedDate === "") {
        alert("Please select a Date.");
        return false;
    } else {
        return true; // Form submission allowed
    }
}



updateMonthYear();
generateCalendar();
