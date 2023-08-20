import React, { useEffect, useState } from "react";
import api from "services/api";

const GuestPage = () => {
  const [data, setData] = useState("");

  useEffect(() => {
    api.get("/user/guest").then((res) => {
      console.log("GuestPage : ", res);
      setData(res.data);
      return res;
    });
  }, []);
  return <div>{data}</div>;
};

export default GuestPage;
