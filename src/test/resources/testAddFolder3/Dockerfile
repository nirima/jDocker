FROM ubuntu
RUN mkdir /usr/local/testing
ADD ./dir/afile.txt /usr/local/testing/afile.txt
ENTRYPOINT ["/bin/cat"]
CMD ["/usr/local/testing/afile.txt"]
