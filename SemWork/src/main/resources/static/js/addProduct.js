document.addEventListener("DOMContentLoaded",()=>{
const form = document.getElementById("product-form");
form.addEventListener("submit", addProduct);
});
function addProduct(event) {
    event.preventDefault();

    const productText = document.getElementById("product-text").value;
    const productImg = document.getElementById("product-img").value;

    const productData = {
        text: productText,
        img: productImg
    };

    let metaTags = document.getElementsByTagName("meta");
    let token, header;

    for (let i = 0; i < metaTags.length; i++) {
        if (metaTags[i].getAttribute("name") === "_csrf") {
            token = metaTags[i].getAttribute("content");
        } else if (metaTags[i].getAttribute("name") === "_csrf_header") {
            header = metaTags[i].getAttribute("content");
        }
    }

    fetch("/products", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            [header]: token
        },
        body: JSON.stringify(productData)
    })
        .then(response => response.json())
        .then(data => {
            window.location.href = "/mainPage"
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Не удалось добавить продукт")
        });
    window.location.href = "/mainPage"
}
