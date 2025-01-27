# Shop App

An application designed for purchasing products, users have the ability to order and view orders. The application dynamically adapts views and functions based on the user type (administrator or regular user). It provides seamless user interaction while handling sensitive scenarios such as login states, product availability and permissions.

## Table of Contents
1. [Technologies](#technologies)
2. [Features](#features)
3. [Screenshots](#screenshots)
4. [Installation](#installation)
5. [License](#license)
6. [Contact](#contact)

## Technologies
- XML
- Datastore Preferences
- Dagger Hilt
- Retrofit
- Clean Architecture

## Features
- **User Roles**:
  - Administrators: Full access to what a regular user has, but also product and user management, i.e. adding and editing.
  - Regular users: Access to view/add products, order, order history and account management.
- **Product Management**:
  - Add, edit or remove products (restrictions apply if the product is in the cart or has been purchased).
- **Order Management**:
  - Detailed order history with product details and total cost.
- **Authentication**:
  - Secure login and registration flow.
  - Redirect to login if the user takes unauthorized actions.
- **User Management (Administrator Only)**:
  - View and modify user roles and information.
- **Dynamic Views**:
  - Profile and functionalities adapt based on user roles.

## Screenshots

<b>1. General view: </br></b>
<img src="https://github.com/user-attachments/assets/83584c7a-c0b4-4c7e-9a2d-ba8728d75371" alt="Home" width="200"/> | <img src="https://github.com/user-attachments/assets/870aa75d-efdf-441e-9209-fcdb50b8a416" alt="Login" width="200"/> | <img src="https://github.com/user-attachments/assets/06306f51-acb8-4c6c-a45f-05f5d8d34274" alt="Register" width="200"/> | <img src="https://github.com/user-attachments/assets/1fdba8fd-4822-4016-9aa8-5f8932bca6c9" alt="Cart" width="200"/> | <img src="https://github.com/user-attachments/assets/944e2f86-cbe8-4f15-a884-327f5a4b8aae" alt="Profile - User View" width="200"/> | <img src="https://github.com/user-attachments/assets/9886d68a-ffda-4a41-9adc-4379608942ba" alt="History Orders" width="200"/> | <img src="https://github.com/user-attachments/assets/db8acb81-0ba1-4674-8c4e-47a26591255e" alt="Change password" width="200"/>

</br></br>

<b>2. Adictional View for Admin: </br></b>
<img src="https://github.com/user-attachments/assets/559d48ef-21d0-478b-bc74-08ff379ba0bc" alt="Profile - Admin View" width="200"/> | <img src="https://github.com/user-attachments/assets/4f5d48a5-616a-4e77-9944-75ac15cf383b" alt="Product Controller" width="200"/> | <img src="https://github.com/user-attachments/assets/1a449e98-575c-4937-89df-922f55eb6f6a" alt="Add/Edit Product" width="200"/> | <img src="https://github.com/user-attachments/assets/449f7794-6201-4db4-8e82-b7ed9abcb0da" alt="User Controller" width="200"/> | <img src="https://github.com/user-attachments/assets/b7f30501-54f4-45be-adef-a45fae7187ee" alt="Add/Edit User" width="200"/>

## Installation

1. Clone mobile app repository:
   ```bash
   git clone https://github.com/Andruszkiewicz-Artur/InternetShopAndroidApp.git

2. Link to back end repository (Version using here is 0.0.1-SNAPSHOT):
   ```bash
   https://github.com/Andruszkiewicz-Artur/InternetShop

## License

The project is available under the MIT license.

## Contact

You can contact me at: andruszkiewiczarturmobileeng@gmail.com
