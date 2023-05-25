function unsubscribeCategory(categoryId) {
    let metaTags = document.getElementsByTagName("meta");
    let token, header;

    for (var i = 0; i < metaTags.length; i++) {
        if (metaTags[i].getAttribute("name") === "_csrf") {
            token = metaTags[i].getAttribute("content");
        } else if (metaTags[i].getAttribute("name") === "_csrf_header") {
            header = metaTags[i].getAttribute("content");
        }
    }

    fetch('/unsubscribe/' + categoryId, {
        method: 'POST',
        credentials: "include",
        headers: {
            [header]: token
        }
    })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                console.error('Failed to unsubscribe from the category');
            }
        })
        .catch(error => {
            console.error('An error occurred:', error);
        });
}

function markProductAsSeen(productId) {
    let metaTags = document.getElementsByTagName("meta");
    let token, header;

    for (let i = 0; i < metaTags.length; i++) {
        if (metaTags[i].getAttribute("name") === "_csrf") {
            token = metaTags[i].getAttribute("content");
        } else if (metaTags[i].getAttribute("name") === "_csrf_header") {
            header = metaTags[i].getAttribute("content");
        }
    }


    fetch('/markAsSeen/' + productId, {
        method: 'POST',
        credentials: "include",
        headers: {
            [header]: token
        }
    })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                console.error('Failed to mark the product as seen');
            }
        })
        .catch(error => {
            console.error('An error occurred:', error);
        });
}

document.addEventListener('DOMContentLoaded', function() {
    const unsubscribeButtons = document.querySelectorAll('.unsubscribeButton');
    const seenButtons = document.querySelectorAll('.seenButton');

    unsubscribeButtons.forEach(function(button) {
        button.addEventListener('click', function(event) {
            const categoryId = button.getAttribute('data-category-id');
            unsubscribeCategory(categoryId);
        });
    });

    seenButtons.forEach(function(button) {
        button.addEventListener('click', function(event) {
            hideElement(event);
            const productId = button.getAttribute('data-product-id');
            markProductAsSeen(productId);
        });
    });
});

function hideElement(event) {
    let element = event.target;
    let parentDiv = element.parentNode;
    parentDiv.remove();
}