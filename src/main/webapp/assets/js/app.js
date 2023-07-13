class App {
    static DOMAIN_SERVER = window.origin;
    static API_SERVER = this.DOMAIN_SERVER + '/api';

    static API_CUSTOMER = this.API_SERVER + '/customers';
    static API_DEPOSIT = this.API_SERVER + '/deposits';
    static API_TRANSFER = this.API_SERVER + '/transfers';

    static API_LOCATION_REGION = 'https://vapi.vnappmob.com/api/province'

    static showDeleteConfirmDialog() {
        return Swal.fire({
            icon: 'warning',
            text: 'Are you sure you want to delete the selected data ?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it !',
            cancelButtonText: 'Cancel',
        });
    }

    static showSuccessAlert(t) {
        Swal.fire({
            icon: 'success',
            title: t,
            position: 'top-end',
            showConfirmButton: false,
            timer: 1500,
        });
    }

    static showErrorAlert(t) {
        Swal.fire({
            icon: 'error',
            title: 'Warning',
            text: t,
        });
    }
}


class LocationRegion {
    constructor(id, provinceId, provinceName, districtId, districtName, wardId, wardName, address) {
        this.id = id;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.address = address;
    }
}

class Customer {
    constructor(id, fullName, email, phone, balance, locationRegion) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
        this.locationRegion = locationRegion;
    }
}

class Deposit {
    constructor(id, customerId, transactionAmount) {
        this.id = id;
        this.customerId = customerId;
        this.transactionAmount = transactionAmount;
    }
}

// $(function() {
//     $(".num-space").number(true, 0, ',', ' ');
//     $(".num-point").number(true, 0, ',', '.');
//     $(".num-comma").number(true, 0, ',', ',');

//     $('[data-toggle="tooltip"]').tooltip();
// });
