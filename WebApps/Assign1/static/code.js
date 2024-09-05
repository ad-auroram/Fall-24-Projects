

const links = Object.values(document.getElementsByClassName("links"));

links.forEach(button =>{
    button.addEventListener("mousedown", (e) => {
        e.target.dataset.pressed = "true";
    });
    button.addEventListener("mouseup", (e) => {
        e.target.dataset.pressed = "false";
    });
    button.addEventListener("mouseleave", (e) => {
        e.target.dataset.pressed = "false";
    });
})

console.log("Help they put me in the console!")