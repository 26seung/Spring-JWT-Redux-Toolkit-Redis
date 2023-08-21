import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { login } from "slice/authSlice";

const SignIn = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [inputs, setInputs] = useState({
    username: "",
    password: "",
  });
  const { username, password } = inputs;

  const onChangeInputs = (e) => {
    setInputs({
      ...inputs,
      [e.target.name]: e.target.value,
    });
  };

  //  로그인 Function
  const handleLogin = (e) => {
    e.preventDefault();
    // redux-tookit
    dispatch(login({ username, password }))
      .unwrap()
      .then((res) => {
        // console.log("SignIn Page response : ", res);
        //  "Home" 페이지로 이동
        navigate("/");
      })
      .catch((err) => {
        // console.log("SignIn Page error : ", err);
      });
  };

  return (
    <>
      <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            stroke="currentColor"
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth="2"
            className="w-12 h-12 text-white p-2 bg-indigo-500 rounded-full mx-auto w-auto"
            viewBox="-2 -2 28 28"
          >
            <path d="M15.59 14.37a6 6 0 01-5.84 7.38v-4.8m5.84-2.58a14.98 14.98 0 006.16-12.12A14.98 14.98 0 009.631 8.41m5.96 5.96a14.926 14.926 0 01-5.841 2.58m-.119-8.54a6 6 0 00-7.381 5.84h4.8m2.581-5.84a14.927 14.927 0 00-2.58 5.84m2.699 2.7c-.103.021-.207.041-.311.06a15.09 15.09 0 01-2.448-2.448 14.9 14.9 0 01.06-.312m-2.24 2.39a4.493 4.493 0 00-1.757 4.306 4.493 4.493 0 004.306-1.758M16.5 9a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0z"></path>
          </svg>
          <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
            Sign in to your account
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form
            className="space-y-6"
            action="#"
            method="POST"
            onSubmit={handleLogin}
          >
            <div>
              <label className="block text-sm font-medium leading-6 text-gray-900">
                Login Id
              </label>
              <div className="mt-2">
                <input
                  id="username"
                  name="username"
                  type="username"
                  placeholder="username"
                  className="border border-gray-300 text-gray-900 sm:text-sm rounded-lg ring-primary-600 border-primary-600 block w-full p-2.5"
                  required
                  value={username}
                  onChange={onChangeInputs}
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label className="block text-sm font-medium leading-6 text-gray-900">
                  Password
                </label>
                {/* <div className="text-sm">
                  <Link
                    href="#"
                    className="font-semibold text-indigo-600 hover:text-indigo-500"
                  >
                    Forgot password?
                  </Link>
                </div> */}
              </div>
              <div className="mt-2">
                <input
                  id="password"
                  name="password"
                  type="password"
                  placeholder="••••••••"
                  autoComplete="current-password"
                  className="border border-gray-300 text-gray-900 sm:text-sm rounded-lg ring-primary-600 border-primary-600 block w-full p-2.5"
                  required
                  value={password}
                  onChange={onChangeInputs}
                />
              </div>
            </div>

            <div>
              <button
                type="submit"
                className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              >
                Sign in
              </button>
            </div>
          </form>

          <p className="mt-10 text-center text-sm text-gray-500">
            Not a member?
            <Link
              to="/auth/signup"
              className="font-semibold leading-6 pl-4 text-indigo-600 hover:text-indigo-500 hover:underline"
            >
              Go to Register
            </Link>
          </p>
        </div>
      </div>
    </>
  );
};

export default SignIn;
