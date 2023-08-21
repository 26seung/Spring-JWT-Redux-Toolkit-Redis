import React, { useCallback, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import {
  logout,
  reissue,
  selectCurrentLoading,
  selectCurrentUser,
} from "slice/authSlice";
import IsLoading from "./IsLoading";

const Header = () => {
  const dispatch = useDispatch();
  const currentUser = useSelector(selectCurrentUser);
  const currentLoading = useSelector(selectCurrentLoading);

  //  로그아웃 Function
  const handleLogout = useCallback(() => {
    dispatch(logout());
  }, [dispatch]);

  //  token 확인
  useEffect(() => {
    const userRefresh = async () => {
      if (!currentUser) {
        await dispatch(reissue());
      }
    };
    userRefresh();
  }, [currentUser, dispatch]);

  //  authSlice. loading state : true 인 경우 loading Spinner
  if (currentLoading) {
    return (
      <>
        <IsLoading />
      </>
    );
  }
  return (
    <>
      <header className="text-gray-600 body-font">
        <div className="container mx-auto flex flex-wrap p-5 flex-col md:flex-row items-center">
          <Link
            to={"/"}
            className="flex title-font font-medium items-center text-gray-900 mb-4 md:mb-0"
          >
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
            <span className="ml-3 text-base">JWT-Redux-Redis</span>
          </Link>
          <nav className="md:ml-auto flex flex-wrap items-center text-base justify-center">
            <Link to={"/user/guest"} className="mr-5 hover:text-gray-900">
              Guest Page
            </Link>
            <Link to={"/user/user"} className="mr-5 hover:text-gray-900">
              User Page
            </Link>
            <Link to={"/user/admin"} className="mr-5 hover:text-gray-900">
              Admin Page
            </Link>
            <Link to={"/"} className="mr-5 hover:text-gray-900">
              None
            </Link>
          </nav>
          {!currentUser ? (
            <div>
              <Link
                to={"/auth/signin"}
                className="inline-flex items-center text-white bg-indigo-500 border-0 py-1 px-3 focus:outline-none hover:bg-indigo-600 rounded text-base mt-4 md:mt-0"
              >
                로그인
              </Link>
              <Link
                to={"/auth/signup"}
                className="inline-flex items-center text-white bg-black border-0 py-1 px-3 focus:outline-none hover:bg-gray-600 rounded text-base mt-4 ml-2 md:mt-0"
              >
                회원가입
              </Link>
            </div>
          ) : (
            <div>
              <Link
                to={"/auth/signin"}
                onClick={handleLogout}
                className="inline-flex items-center text-white bg-black border-0 py-1 px-3 focus:outline-none hover:bg-gray-600 rounded text-base mt-4 ml-2 md:mt-0"
              >
                로그아웃
              </Link>
            </div>
          )}
        </div>
      </header>
    </>
  );
};

export default Header;
