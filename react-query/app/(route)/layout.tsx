import { Montserrat } from "next/font/google";

const font = Montserrat({ weight: "600", subsets: ["latin"] });

const Layout = ({ children }: { children: React.ReactNode }) => {
  return <>{children}</>;
};
export default Layout;
