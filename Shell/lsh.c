#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <signal.h>
#include <stdbool.h>

void c_handler(){
    signal(SIGINT,c_handler);
}

void zombie_handler()
{
    wait(NULL);
}

int main(int argc, char *argv[]) {
    int i,j,pipe_count;
    char line[100];
    char *comm[50][50];
    bool bg;
    int status;
    signal(SIGINT,c_handler);
    signal(SIGCHLD,zombie_handler);
    while(1){
        bg=false;
        printf("$ ");
        fgets(line,100,stdin);
        line[strlen(line)-1]='\0'; 
        if (feof(stdin) || strcmp(line,"exit")==0){
            printf("shell closed\n");
            exit(0);
        }
        char *ptr = strtok(line," ");
        i=0;
        j=0;
        pipe_count=0;
	    while(ptr != NULL)
	    {
            if(strcmp(ptr,"&")==0){
                comm[i][j]=NULL;
                bg=true;
                break;
            }
            else if(strcmp(ptr,"|")==0){
                pipe_count+=1;
                comm[i][j]=NULL;
                i++;
                j=0;
                comm[i][j]=ptr;
                ptr = strtok(NULL," ");
            }
            else{
                comm[i][j]=ptr;
                j++;
		        ptr = strtok(NULL," ");
            }
	    }
        comm[i][j]=NULL;
        if(pipe_count==0){
            pid_t pid;
            pid=fork();
            if(strcmp(comm[0][0],"cd")==0){
                chdir(comm[0][1]);
            }
            else if (pid == 0){
                i=execvp(comm[0][0],comm[0]);
            }
            if(bg==false){
                waitpid(pid,&status,0);
            }
        }
    }
}