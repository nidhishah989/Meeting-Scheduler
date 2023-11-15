
const city_input = document.querySelector("#city");
const cityvaluemessage = document.querySelector('#cityvaluemessage');
city_input.addEventListener("input",function(){
    const value = city_input.value;
    const city_pattern = /[A-Za-z]+$/;
    if(city_pattern.test(value)){
        cityvaluemessage.textContent ='';
    }
    else{
        cityvaluemessage.textContent="Invalid city name.";
    }
});

