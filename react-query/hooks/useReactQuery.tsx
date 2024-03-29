"use client";

import {
  QueryClient,
  QueryClientProvider,
  useQuery,
} from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import axios from "axios";

export const ReactQueryProviders = ({ children }: React.PropsWithChildren) => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: 60 * 1000,
      },
    },
  });
  return (
    <QueryClientProvider client={queryClient}>
      {children}
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
};

// 해당 채팅에 대한 메시지들을 리턴
export const useGuest = () => {
  return useQuery({
    queryKey: ["guest"],
    queryFn: async () => {
      const response = await axios.get("http://localhost:8080/api/user/guest");
      return response.data;
    },
  });
};
