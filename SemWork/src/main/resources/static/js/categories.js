document.addEventListener('DOMContentLoaded',()=>{
    const categoryContainer = document.querySelector('#categoryContainer');
let metaTags = document.getElementsByTagName("meta");
let token, header;

    for (let i = 0; i < metaTags.length; i++) {
        if (metaTags[i].getAttribute("name") === "_csrf") {
            token = metaTags[i].getAttribute("content");
        } else if (metaTags[i].getAttribute("name") === "_csrf_header") {
            header = metaTags[i].getAttribute("content");
        }
    }

    fetch('/categories', {
        method: 'GET',
        credentials: "include",
        headers: {
            [header]: token
        }
    }) .then(response => response.json())
        .then(data => {
            for (let i = 0; i < data.length; i++){
                let div = document.createElement("div");
                let category = data[i];
                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.id = `category-${category.id}`;
                checkbox.name = 'category';
                checkbox.value = category.id;
                checkbox.addEventListener('change', filterProducts);

                const label = document.createElement('label');
                label.setAttribute('for', `category-${category.id}`);
                label.textContent = category.name;
                div.append(checkbox, label);
                categoryContainer.appendChild(div);
            }

        })
        .catch(error => console.error(error));
})
var wishlistsInput = document.getElementById("wishlists");
var wishlistsValue = wishlistsInput.value;



function filterProducts() {
    const checkboxes = document.querySelectorAll('input[name=category]:checked');
    const selectedCategories = Array.from(checkboxes).map(checkbox => Number(checkbox.value));
    let metaTags = document.getElementsByTagName("meta");
    let token, header;

    for (let i = 0; i < metaTags.length; i++) {
        if (metaTags[i].getAttribute("name") === "_csrf") {
            token = metaTags[i].getAttribute("content");
        } else if (metaTags[i].getAttribute("name") === "_csrf_header") {
            header = metaTags[i].getAttribute("content");
        }
    }

    fetch('/products/filter', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        },
        body: JSON.stringify(selectedCategories)
    })
        .then(response => response.json())
        .then(filteredProducts => {
            const productCards = document.querySelector('.productCards');
            const filteredProductCards = document.createElement('div');
            filteredProductCards.classList.add("productCards");

            filteredProducts.forEach(product => {
                const productCard = document.createElement('div');
                productCard.className = 'productCard';

                const img = document.createElement('img');
                img.className = 'imgCard';
                img.src = product.img;
                productCard.appendChild(img);

                const text = document.createElement('p');
                text.className = 'imgText';
                text.textContent = product.text;
                productCard.appendChild(text);

                const dropdownLists = document.createElement('div');
                dropdownLists.className = 'dropdownLists';

                const dropListsBtn = document.createElement('button');
                dropListsBtn.className = 'dropListsBtn';

                const heartIcon = document.createElement('i');
                heartIcon.className = 'far fa-heart fa-1x';
                dropListsBtn.appendChild(heartIcon);
                dropdownLists.appendChild(dropListsBtn);

                const dropdownListsContainer = document.createElement('div');
                dropdownListsContainer.className = 'dropdown-lists dropLists';

                dropListsBtn.addEventListener("click", ()=>{
                    dropdownListsContainer.classList.toggle("show");
                })


                const addWishlistForm = document.createElement('form');
                addWishlistForm.setAttribute('action', '/addWishlist');
                addWishlistForm.setAttribute('method', 'get');

                const addWishlistSubmit = document.createElement('input');
                addWishlistSubmit.className = 'wish';
                addWishlistSubmit.setAttribute('type', 'submit');
                addWishlistSubmit.setAttribute('value', '+');
                addWishlistForm.appendChild(addWishlistSubmit);

                dropdownListsContainer.appendChild(addWishlistForm);
                dropdownLists.appendChild(dropdownListsContainer);
                productCard.appendChild(dropdownLists);


                var regex = /WishlistDto\((.*?)\)/g;
                var matches;
                var wishlistsArray = [];

                while ((matches = regex.exec(wishlistsValue)) !== null) {
                    var wishlistString = matches[1];
                    var wishlistParts = wishlistString.split(", ");

                    var wishlist = {
                        id: parseInt(wishlistParts[0].split("=")[1]),
                        title: wishlistParts[1].split("=")[1],
                        description: wishlistParts[2].split("=")[1],
                        userId: parseInt(wishlistParts[3].split("=")[1]),
                        productIds: wishlistParts[4].split("=")[1].replace("[", "").replace("]", "").split(",").map(Number)
                    };

                    wishlistsArray.push(wishlist);
                }

                wishlistsArray.forEach(function(wishlist) {

                    const wishlistItem = document.createElement('div');
                    wishlistItem.id = 'addProductForm';

                    const wishlistSubmit = document.createElement('input');
                    wishlistSubmit.className = 'wish';
                    wishlistSubmit.id = 'addProductButton';
                    wishlistSubmit.setAttribute('type', 'submit');
                    wishlistSubmit.setAttribute('value', wishlist.title);
                    wishlistItem.appendChild(wishlistSubmit);

                    const listIDInput = document.createElement('input');
                    listIDInput.setAttribute('type', 'hidden');
                    listIDInput.id = 'listID';
                    listIDInput.setAttribute('name', 'listID');
                    listIDInput.setAttribute('value', wishlist.id.toString());
                    wishlistItem.appendChild(listIDInput);

                    const prodIDInput = document.createElement('input');
                    prodIDInput.setAttribute('type', 'hidden');
                    prodIDInput.id = 'prodID';
                    prodIDInput.setAttribute('name', 'prodID');
                    prodIDInput.setAttribute('value', product.id);
                    wishlistItem.appendChild(prodIDInput);

                    dropdownListsContainer.appendChild(wishlistItem);

                });

                filteredProductCards.appendChild(productCard);
            });

            productCards.innerHTML = '';
            productCards.appendChild(filteredProductCards);
        })
        .catch(error => console.error(error));
}


