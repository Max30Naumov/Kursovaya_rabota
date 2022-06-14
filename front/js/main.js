const HOST = 'http://localhost:8001/back?';

const KEY_STORE_NAME = 'storeName';
const KEY_STORE_ADDRESS = 'storeAddress';
const KEY_CODE = 'code';
const KEY_NAME = 'name';
const KEY_QUANTITY = 'quantity';
const KEY_PRICE = 'price';
const KEY_SUM = 'sum';

let JsonArrOriginal = [];
let JsonArr = [];

fetch(HOST + "start", {
    method: 'POST',
}).then(response =>
    response.json().then(data => ({
        data: data,
    })).then(res => {
        try {
            JsonArr = res.data;
            console.log(JsonArr);

            for (i = 0; i < JsonArr.length; i++) {
                JsonArr[i][KEY_SUM] = JsonArr[i][KEY_PRICE] * JsonArr[i][KEY_QUANTITY];
            }

            JsonArrOriginal = JsonArr;
            render(JsonArr);
        } catch {
            JsonArr = [];
        }
    }));

let $content_table = document.querySelector('.content_table')

function render(array) {
    $content_table.innerHTML = '<thead>\n' +
        '<tr>\n' +
        '<th scope="col">Название магазина</th>\n' +
        '<th scope="col">Адрес магазина</th>\n' +
        '<th scope="col">Код товара</th>\n' +
        '<th scope="col">Название</th>\n' +
        '<th scope="col">Кол-во</th>\n' +
        '<th scope="col">Цена за ед.</th>\n' +
        '<th scope="col">Сумма</th>\n' +
        '</tr>\n' +
        '</thead>';
    for (i = 0; i < array.length; i++) {
        $content_table.innerHTML += '<tbody><tr><td>' +
            array[i][KEY_STORE_NAME] + '</td><td>' +
            array[i][KEY_STORE_ADDRESS] + '</td><td>' +
            array[i][KEY_CODE] + '</td><td>' +
            array[i][KEY_NAME] + '</td><td>' +
            array[i][KEY_QUANTITY] + '</td><td>' +
            array[i][KEY_PRICE] + '</td><td>' +
            array[i][KEY_SUM] +
            '</td></tr></tbody>'
    }
}

function byField(field) {
    return (a, b) => a[field] < b[field] ? 1 : -1;
}
function filter(category) {
    JsonArr.sort(byField(category));
    JsonArr.reverse();
}

filter("name");
render(JsonArr);

document.querySelector('#filter_sum').addEventListener('change', function() {
    if (this.checked) {
        filter('sum');
    }
    render(JsonArr);
});
document.querySelector('#filter_name').addEventListener('change', function() {
    if (this.checked) {
        filter('name');
    }
    render(JsonArr);
});

let $search_name = document.querySelector('#search_name');
$search_name.addEventListener('input', function() {
    let result = [];
    for (let i = 0; i < JsonArr.length; i++) {
        if (JsonArr[i][KEY_NAME].toLowerCase().includes($search_name.value.toLowerCase())) {
            result.push(JsonArr[i]);
        }
    }
    render(result);
})

document.querySelector('#modal_add_send').addEventListener('click', function() {
    let result = {
        storeName: document.querySelector('#modal_input_store_name').value,
        storeAddress: document.querySelector('#modal_input_store_address').value,
        code: Number(document.querySelector('#modal_input_code').value),
        name: document.querySelector('#modal_input_name').value,
        quantity: Number(document.querySelector('#modal_input_quantity').value),
        price: Number(document.querySelector('#modal_input_price').value),
    };
    fetch(HOST + "add", {
        method: 'POST',
        body: JSON.stringify(result),
    }).then(response =>
        response.json().then(data => ({
            data: data,
        })).then(res => {
            console.log(result);
            if (res.data["added_successfully"] === true) {
                result[KEY_SUM] = result[KEY_PRICE] * result[KEY_QUANTITY];
                JsonArr.push(result);
                render(JsonArr);
                alert("Успешно добавлено");
                const addModalWrapper = document.querySelector('#addModal');
                const addModal = bootstrap.Modal.getInstance(addModalWrapper);
                addModal.hide();
                document.querySelector('#modal_input_store_name').value = "";
                document.querySelector('#modal_input_store_address').value = "";
                document.querySelector('#modal_input_code').value = "";
                document.querySelector('#modal_input_name').value = "";
                document.querySelector('#modal_input_quantity').value = "";
                document.querySelector('#modal_input_price').value = "";
            } else {
                alert("Неправильные данные")
            }
        }));
})
document.querySelector('#modal_delete_send').addEventListener('click', function() {
    let result = [];
    let priceToDelete = Number(document.querySelector('#modal_delete_input').value);
    for (i = 0; i < JsonArr.length; i++) {
        if (JsonArr[i][KEY_PRICE] !== priceToDelete) {
            result.push(JsonArr[i]);
        }
    }
    JsonArr = result;
    render(JsonArr);
    for (i = 0; i < JsonArr.length; i++) {
        delete JsonArr[i].sum;
    }

    fetch(HOST + "delete", {
        method: 'POST',
        body: JSON.stringify(result),
    }).then(response =>
        response.json().then(data => ({
            data: data,
            status: response.status
        })).then(res => {
            if (res.data["deleted_successfully"] === true) {
                const deleteModalWrapper = document.querySelector('#deleteModal');
                const deleteModal = bootstrap.Modal.getInstance(deleteModalWrapper);
                deleteModal.hide();
                render(JsonArr);
                alert("Товар успешно удален");
            } else {
                alert("Ошибка во время удаления")
            }
        }));
})


let edit_id = -1;
document.querySelector('#edit_element').addEventListener('change', function() {
    let product_found = false;
    let product_edit_name = Number(document.querySelector('#edit_element').value);
    for (i = 0; i < JsonArrOriginal.length; i++) {
        if (JsonArrOriginal[i][KEY_CODE] === product_edit_name) {
            let editModalWrapper = document.querySelector('#editModal');
            let editModal = new bootstrap.Modal(editModalWrapper);
            editModal.show();
            edit_id = i;
            product_found = true;
            break;
        }
    }
    if (product_found === false) {
        alert("Товар с таким кодом не найден");
    }
})

document.querySelector('#modal_edit_send').addEventListener('click', function() {
    let result = {
        storeName: document.querySelector('#modal_input_store_name_edit').value,
        storeAddress: document.querySelector('#modal_input_store_address_edit').value,
        code: Number(document.querySelector('#modal_input_code_edit').value),
        name: document.querySelector('#modal_input_name_edit').value,
        quantity: Number(document.querySelector('#modal_input_quantity_edit').value),
        price: Number(document.querySelector('#modal_input_price_edit').value),
    };

    let editData = {
        id: edit_id,
        product: result
    }
    fetch(HOST + "edit", {
        method: 'POST',
        body: JSON.stringify(editData),
    }).then(response =>
        response.json().then(data => ({
            data: data,
        })).then(res => {
            if (JSON.stringify(res.data) === '{"edited_successfully":true}') {
                result[KEY_SUM] = result[KEY_PRICE] * result[KEY_QUANTITY];
                JsonArrOriginal[edit_id] = result;
                render(JsonArrOriginal);
                alert("Успешно отредактировано");
                const editModalWrapper = document.querySelector('#editModal');
                const editModal = bootstrap.Modal.getInstance(editModalWrapper);
                editModal.hide();
                document.querySelector('#modal_input_store_name_edit').value = "";
                document.querySelector('#modal_input_store_address_edit').value = "";
                document.querySelector('#modal_input_code_edit').value = "";
                document.querySelector('#modal_input_name_edit').value = "";
                document.querySelector('#modal_input_quantity_edit').value = "";
                document.querySelector('#modal_input_price_edit').value = "";
            } else {
                alert("Неправильные данные")
            }
        }));

})

document.querySelector('#excerpt_element').addEventListener('change', function() {
    let product_excerpt_name = document.querySelector('#excerpt_element').value;
    let sum_price = 0;

    let excerptResults = [];
    for (let i = 0; i < JsonArr.length; i++) {
        if (JsonArr[i][KEY_NAME] === product_excerpt_name) {
            excerptResults.push(JsonArr[i]);
            sum_price += JsonArr[i][KEY_SUM];
        }
    }
    if (excerptResults.length > 0) {

        filter("price");
        let cheapest_price_address = JsonArr[0][KEY_STORE_ADDRESS];
        let cheapestAddressShow = document.querySelector('#cheapest_address');
        cheapestAddressShow.innerHTML = cheapest_price_address;

        let sumPriceShow = document.querySelector('#sum_price');
        sumPriceShow.innerHTML = sum_price.toString();

        let excerptModalWrapper = document.querySelector('#excerptModal');
        let excerptModal = new bootstrap.Modal(excerptModalWrapper);
        excerptModal.show();

    } else {
        alert("Ни один товар с таким названием не найден");
    }

})