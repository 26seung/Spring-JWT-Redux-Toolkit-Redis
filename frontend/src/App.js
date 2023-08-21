import { Route, Routes } from "react-router-dom";
import Header from "components/Header";
import Footer from "components/Footer";
import SignIn from "pages/auth/SignIn";
import SignUp from "pages/auth/SignUp";
import NotFound from "pages/error/NotFound";
import UserPage from "pages/user/UserPage";
import GuestPage from "pages/user/GuestPage";
import AdminPage from "pages/user/AdminPage";

function App() {
  //  main page Router
  return (
    <div className="container mx-auto max-w-screen-lg">
      <Header />
      <div className="min-h-screen">
        <Routes>
          <Route exaat path="/" element={""} />
          <Route exaat path="*" element={<NotFound />} />
          {/* Auth */}
          <Route exaat path="/auth/signin" element={<SignIn />} />
          <Route exaat path="/auth/signup" element={<SignUp />} />
          {/* User */}
          <Route exaat path="/user/guest" element={<GuestPage />} />
          <Route exaat path="/user/user" element={<UserPage />} />
          <Route exaat path="/user/admin" element={<AdminPage />} />
        </Routes>
      </div>
      <Footer />
    </div>
  );
}

export default App;
