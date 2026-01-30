async function isLogged(){
    const response = await fetch("http://localhost:8080/api/auth/@me")
    if(response.ok){
        window.location.href = "/index.html"
    }
}

isLogged()

const button = document.getElementById("login")

button.addEventListener("click", async function(){
    const email = document.getElementById("email").value
    const password = document.getElementById("password").value

    console.log(email)
    console.log(password)

    const response = await fetch("http://localhost:8080/api/auth/login",{
        method:"POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({email: email, password: password})
    })


    if(response.ok){
        window.location.href = "/index.html"
    }
    document.getElementById("password").value = ""

})